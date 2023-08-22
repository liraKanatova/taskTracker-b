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
import peaksoft.house.tasktrackerb9.dto.request.ParticipantsRequest;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.enums.Role;
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
        WorkSpace workSpace = workSpaceRepository.findById(request.workSpacesId()).orElseThrow(() -> {
            log.error("With workSpaces id " + request.workSpacesId() + " not found");
            return new NotFoundException("With workSpaces id " + request.workSpacesId() + " not found");
        });
        if (userRepository.existsByEmail(request.email())) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject("Invite to workSpaces ");
            helper.setTo(request.email());
            javaMailSender.send(mimeMessage);
            if (!user.getWorkSpaces().contains(workSpace)) {
                throw new NotFoundException("this your no workSpaces");
            }
            if (request.role().equals(Role.ADMIN)) {
                helper.setText(request.link() + "/" + request.role() + "/workspaceId/" + request.workSpacesId());
            } else if (request.role().equals(Role.MEMBER)) {
                helper.setText(request.link() + "/" + request.role() + "/workspaceId/" + request.workSpacesId());
            }
            UserWorkSpaceRole userWorkSpaceRole = new UserWorkSpaceRole();
            userWorkSpaceRole.setMember(user);
            userWorkSpaceRole.setRole(request.role());
            userWorkSpaceRole.setWorkSpace(workSpace);
            userWorkSpaceRoleRepository.save(userWorkSpaceRole);
        }else {
            throw new NotFoundException("With user email "+request.email()+" not found");
        }
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully invite")
                .build();
    }

    @Override
    public SimpleResponse removeToWorkSpaces(Long workSpacesId, Long userId) {
        return null;
    }

    @Override
    public SimpleResponse changeUpdateRole(Long workSpacesId, Long userId, Role role) {
        return null;
    }

    @Override
    public List<ParticipantsResponse> getAllParticipants(Long workSpacesId) {
        return customParticipantsRepository.getAllParticipants(workSpacesId);
    }

    @Override
    public List<ParticipantsResponse> getAllAdminParticipants(Long workSpacesId) {
        return customParticipantsRepository.getAllAdminParticipants(workSpacesId);
    }

    @Override
    public List<ParticipantsResponse> getAllMemberParticipants(Long workSpacesId) {
        return customParticipantsRepository.getAllMemberParticipants(workSpacesId);
    }
}
