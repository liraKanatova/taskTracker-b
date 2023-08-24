package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.ChangeRoleRequest;
import peaksoft.house.tasktrackerb9.dto.request.InviteRequest;
import peaksoft.house.tasktrackerb9.dto.response.AllMemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.exceptions.AlreadyExistException;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.*;
import peaksoft.house.tasktrackerb9.repositories.*;
import peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl.CustomMemberRepositoryImpl;
import peaksoft.house.tasktrackerb9.services.MemberService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final UserWorkSpaceRoleRepository userWorkSpaceRoleRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final WorkSpaceRepository workSpaceRepository;
    private final CardRepository cardRepository;
    private final CustomMemberRepositoryImpl customMemberRepository;
    private final JavaMailSender javaMailSender;

    @Override
    public List<MemberResponse> searchByEmail(Long workSpaceId, String email) {
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId)
                .orElseThrow(() -> {
                    log.error("WorkSpace with id: " + workSpaceId + " not found");
                    throw new NotFoundException("WorkSpace with id: " + workSpaceId + " not found");
                });
        return customMemberRepository.searchByEmail(workSpace.getId(), email);
    }

    @Override
    public AllMemberResponse getAll(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> {
            log.error("Card with id: " + cardId + " not found");
            throw new NotFoundException("Card with id: " + cardId + " not found");
        });
        return customMemberRepository.getAll(card.getId());
    }

    public SimpleResponse inviteMemberToBoard(InviteRequest request) throws MessagingException {
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> {
                    log.error("Board with id: " + request.getBoardId() + " not found");
                    throw new NotFoundException("Board with id: " + request.getBoardId() + " not found");
                });
        WorkSpace workSpace = workSpaceRepository.findById(board.getWorkSpace().getId())
                .orElseThrow(() -> {
                    log.error("WorkSpace with id: " + board.getWorkSpace().getId() + " not found");
                    throw new NotFoundException("WorkSpace with id: " + board.getWorkSpace().getId() + " not found");
                });
        if (userRepository.existsByEmail(request.getEmail())) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject("Invite new member to board!");
            helper.setTo(request.getEmail());
            helper.setText("Board id: " + request.getBoardId() + " role : " + request.getRole() + " link : " + request.getLink());
            javaMailSender.send(mimeMessage);
            User user = userRepository.findUserByEmail(request.getEmail())
                    .orElseThrow(() -> new NotFoundException("User with email: " + request.getEmail() + " not found"));
            UserWorkSpaceRole userWorkSpace = new UserWorkSpaceRole();
            userWorkSpace.setMember(user);
            userWorkSpace.setRole(request.getRole());
            board.setWorkSpace(workSpace);
            userWorkSpace.setWorkSpace(board.getWorkSpace());
            userWorkSpaceRoleRepository.save(userWorkSpace);
            board.getMembers().add(user);
        } else throw new NotFoundException(String.format("User with email: %s is not found", request.getEmail()));
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Message sent successfully!")
                .build();
    }

    @Override
    public SimpleResponse changeMemberRole(ChangeRoleRequest request) {
        User u = jwtService.getAuthentication();
        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(() -> {
                    log.error("Board with id: " + request.boardId() + " not found");
                    throw new NotFoundException("Board with id: " + request.boardId() + " not found");
                });
        User user = userRepository.findById(request.memberId())
                .orElseThrow(() -> {
                    log.error("User with id : " + request.memberId() + " not found");
                    throw new NotFoundException("User with id " + request.memberId() + " not found");
                });
        List<UserWorkSpaceRole> workSpaceRole = userWorkSpaceRoleRepository.findByUserId(board.getId(), user.getId());
        WorkSpace workSpace = board.getWorkSpace();
        if (!workSpace.getAdminId().equals(u.getId())) {
            throw new BadCredentialException("You are not a admin of this workSpace");
        }
        for (UserWorkSpaceRole w : workSpaceRole) {
            for (Board b : w.getWorkSpace().getBoards()) {
                if (b.getId().equals(request.boardId())) {
                    w.setRole(request.role());
                    userWorkSpaceRoleRepository.save(w);
                    return SimpleResponse.builder()
                            .status(HttpStatus.OK)
                            .message("Member role changed successfully!")
                            .build();
                }
            }
        }
        throw new NotFoundException("Board with id : " + request.boardId() + " not found in the workSpace");
    }

    @Override
    public List<MemberResponse> getAllMembersFromBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.error("Board with id: " + boardId + " not found");
                    throw new NotFoundException("Board with id: " + boardId + " not found");
                });
        return customMemberRepository.getAllMembersFromBoard(board.getId());
    }


    @Override
    public SimpleResponse assignMemberToCard(Long memberId, Long cardId) {
        User user = jwtService.getAuthentication();
        Long adminId = cardRepository.getUserIdByCardId(cardId).orElseThrow(() -> {
                    log.error("This card with id: "+cardId+ " is not present in your workspace!");
                    return new BadCredentialException("This card with id: "+cardId+ " is not present in your workspace!");
                }
        );
        if (!user.getId().equals(adminId)) {
            log.error("You do not have permission to assign members to this card!");
            throw new BadCredentialException("You do not have permission to assign members to this card!");
        }
        WorkSpace workSpace = cardRepository.getWorkSpaceByCardId(cardId).orElseThrow(() -> {
            log.error("This card with id: %s not in this workSpace ".formatted(cardId));
            return new NotFoundException("This card with id: %s not in this workSpace ".formatted(cardId));
        });
        List<Long> userIds = userRepository.getAllUsersByWorkSpaseId(workSpace.getId());
        boolean isFalse = false;
        for (Long l : userIds) {
            isFalse = !memberId.equals(l);
        }
        if (isFalse) {
            log.error("User with  id: %s is not on your workSpace");
            throw new NotFoundException("User with  id: %s is not on your workSpace".formatted(memberId));
        }
       List<Long> getUserIdsByCardId = cardRepository.getMembersByCardId(cardId);
        boolean isTrue = getUserIdsByCardId.stream().anyMatch(id -> id.equals(memberId));
        if (isTrue) {
            throw new AlreadyExistException("User with id: %d exists".formatted(memberId));
        }
        User newMember = userRepository.findById(memberId).orElseThrow(
                () -> {
                    log.error("User with id: %s not found".formatted(memberId));
                    return new NotFoundException("User with id: %s not found".formatted(memberId));
                });
        Card card = cardRepository.findById(cardId).orElseThrow(() -> {
            log.error("Card with id: %s not found".formatted(cardId));
            return new NotFoundException("Card with id: %s not found".formatted(cardId));
        });
        card.getMembers().add(newMember);
        newMember.getCards().add(card);
        userRepository.save(newMember);
        cardRepository.save(card);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("User with id : %d successfully assigned to card with id : %d", memberId, cardId))
                .build();
    }
}