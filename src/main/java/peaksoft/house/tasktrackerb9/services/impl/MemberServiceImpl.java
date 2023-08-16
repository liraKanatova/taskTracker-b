package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.request.ChangeRoleRequest;
import peaksoft.house.tasktrackerb9.dto.request.InviteRequest;
import peaksoft.house.tasktrackerb9.dto.response.AllMemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
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
        if (userRepository.existsByEmail(request.getEmail())) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject("Invite new member to board!");
            helper.setTo(request.getEmail());
            helper.setText("Board id: " + request.getBoardId() + "/n role : " + request.getRole() + "/n link : " + request.getLink());
            javaMailSender.send(mimeMessage);
            User user = userRepository.findUserByEmail(request.getEmail())
                    .orElseThrow(() -> new NotFoundException("User with email: " + request.getEmail() + " not found"));
            UserWorkSpaceRole userWorkSpace = new UserWorkSpaceRole();
            userWorkSpace.setMember(user);
            userWorkSpace.setRole(request.getRole());
            userWorkSpace.setWorkSpace(board.getWorkSpace());
            userWorkSpaceRoleRepository.save(userWorkSpace);
            board.getMembers().add(user);
        } else throw new NotFoundException(String.format("User with email:%s is not found", request.getEmail()));
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Message sent successfully!")
                .build();
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
    public SimpleResponse changeMemberRole(ChangeRoleRequest request) {
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
        return null;
    }


}