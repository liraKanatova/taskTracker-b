package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.ParticipantsChangeRequest;
import peaksoft.house.tasktrackerb9.dto.request.ParticipantsRequest;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.models.UserWorkSpaceRole;
import peaksoft.house.tasktrackerb9.models.WorkSpace;
import peaksoft.house.tasktrackerb9.repositories.UserRepository;
import peaksoft.house.tasktrackerb9.repositories.UserWorkSpaceRoleRepository;
import peaksoft.house.tasktrackerb9.repositories.WorkSpaceRepository;
import peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl.CustomParticipantsRepositoryImpl;
import peaksoft.house.tasktrackerb9.services.ParticipantsService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ParticipantsServiceImpl implements ParticipantsService {

    private final UserWorkSpaceRoleRepository userWorkSpaceRoleRepository;

    private final CustomParticipantsRepositoryImpl customParticipantsRepository;

    private final WorkSpaceRepository workSpaceRepository;

    private final JavaMailSender javaMailSender;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Override
    public SimpleResponse inviteToWorkSpaces(ParticipantsRequest request) throws MessagingException {
        User user = jwtService.getAuthentication();
        WorkSpace workSpace = workSpaceRepository.findById(request.workSpacesId()).orElseThrow(() ->
                new NotFoundException("Workspace with id " + request.workSpacesId() + " not found"));
        if (!user.getWorkSpaces().contains(workSpace)) {
            throw new NotFoundException("You are not associated with this workspace");
        }
        User user1 = userRepository.findUserByEmailParticipants(request.email());
        if (user1 == null) {
            throw new NotFoundException("User with email " + request.email() + " not found");
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setSubject("Invite to workspace");
        helper.setTo(request.email());
        String invitationText = request.link() + "/" + request.role() + "/workspaceId/" + request.workSpacesId();
        helper.setText(invitationText);
        javaMailSender.send(mimeMessage);
        UserWorkSpaceRole userWorkSpaceRole = new UserWorkSpaceRole();
        userWorkSpaceRole.setMember(user1);
        userWorkSpaceRole.setRole(request.role());
        userWorkSpaceRole.setWorkSpace(workSpace);
        userWorkSpaceRoleRepository.save(userWorkSpaceRole);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully invited")
                .build();
    }

    @Override
    public SimpleResponse removeToWorkSpaces(Long workSpaceId,Long userId) {
        User authentication = jwtService.getAuthentication();
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId)
                .orElseThrow(() -> new NotFoundException("Workspace with id " + workSpaceId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        if (!workSpace.getAdminId().equals(authentication.getId())) {
            throw new NotFoundException("You are not the admin of this workspace");
        }
        List<UserWorkSpaceRole> userWorkSpaceRoles = userWorkSpaceRoleRepository.findByUserToWorkSpace(user.getId(), workSpace.getId());
        for (UserWorkSpaceRole workSpaceRole : userWorkSpaceRoles) {
            if (workSpaceRole.getWorkSpace().equals(workSpace) && workSpaceRole.getMember().equals(user)) {
                userWorkSpaceRoleRepository.deleteById(workSpaceRole.getId());
                log.info("Successfully deleted");
            }else throw new BadCredentialException("Not found id");
        }
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully deleted" )
                .build();
    }

    @Override
    public SimpleResponse changeUpdateRole(ParticipantsChangeRequest request) {
        User authentication = jwtService.getAuthentication();
        WorkSpace workSpace = workSpaceRepository.findById(request.workSpacesId())
                .orElseThrow(() -> new NotFoundException("Workspace with id " + request.workSpacesId() + " not found"));

        User user = userRepository.findById(request.memberId())
                .orElseThrow(() -> new NotFoundException("User with id " + request.memberId() + " not found"));

        if (!workSpace.getAdminId().equals(authentication.getId())) {
            throw new NotFoundException("You are not the admin of this workspace");
        }
        List<UserWorkSpaceRole> userWorkSpaceRoles = userWorkSpaceRoleRepository.findByUserToWorkSpace(user.getId(), workSpace.getId());
        for (UserWorkSpaceRole userWorkSpaceRole : userWorkSpaceRoles) {
            if (userWorkSpaceRole.getWorkSpace().getId().equals(request.workSpacesId())) {
                userWorkSpaceRole.setRole(request.role());
                userWorkSpaceRoleRepository.save(userWorkSpaceRole);
                log.info("Updated change role");
            }
        }
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully role  updated")
                .build();
    }

    @Override
    public List<ParticipantsResponse> getParticipantsByRole(Long workSpaceId, Role role) {
        return customParticipantsRepository.getParticipantsByRole(workSpaceId,role);
    }
}
