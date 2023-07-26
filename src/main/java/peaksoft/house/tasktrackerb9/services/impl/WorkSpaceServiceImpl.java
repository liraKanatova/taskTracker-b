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
import peaksoft.house.tasktrackerb9.dto.request.WorkSpaceRequest;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceResponse;
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.models.UserWorkSpaceRole;
import peaksoft.house.tasktrackerb9.models.WorkSpace;
import peaksoft.house.tasktrackerb9.repositories.UserRepository;
import peaksoft.house.tasktrackerb9.repositories.UserWorkSpaceRoleRepository;
import peaksoft.house.tasktrackerb9.repositories.WorkSpaceRepository;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.WorkSpaceJdbcTemplateService;
import peaksoft.house.tasktrackerb9.services.WorkSpaceService;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WorkSpaceServiceImpl implements WorkSpaceService {

    private final WorkSpaceRepository workSpaceRepository;
    private final JwtService jwtService;
    private final UserWorkSpaceRoleRepository userWorkSpaceRoleRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final WorkSpaceJdbcTemplateService workSpaceJdbcTemplateService;


    @Override
    public List<WorkSpaceResponse> getAllWorkSpaces() {
     return   workSpaceJdbcTemplateService.getAllWorkSpaces();
    }

    @Override
    public SimpleResponse saveWorkSpace(WorkSpaceRequest request) throws MessagingException {
        User user = jwtService.getAuthentication();
        WorkSpace workspace = new WorkSpace(request.getName(),user.getId());
        UserWorkSpaceRole userWorkSpace = new UserWorkSpaceRole(Role.ADMIN,user,workspace);
        user.setRoles(List.of(userWorkSpace));
        user.setWorkSpaces(List.of(workspace));
        workspace.setUsers(List.of(user));
        workSpaceRepository.save(workspace);
        List<String> invitationEmails = request.getEmails();
        if (!invitationEmails.isEmpty() && !invitationEmails.get(0).isBlank()) {
            for (String email : invitationEmails) {
                if (!userRepository.existsByEmail(email)) {
                    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    helper.setSubject(" Welcome to my workspace");
                    helper.setFrom("tasktrackerjava9@gmail.com");
                    helper.setTo(email);
                    helper.setText("/workspaceId/" + workspace.getId()+" Click link to register :"+request.getLink());
                    javaMailSender.send(mimeMessage);
                    log.info(String.format("WorkSpace with name %s successfully saved!", workspace.getName()));
                    return SimpleResponse.builder()
                            .status(HttpStatus.OK)
                            .message(String.format("WorkSpace with name %s successfully saved!", workspace.getName()))
                            .build();
                }
            }
        }
        log.info("Email doesn't exist !");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Email doesn't exist !")
                .build();
    }

    @Override
    public WorkSpaceResponse getWorkSpaceById(Long id) {
     return workSpaceJdbcTemplateService.getWorkSpaceById(id);
    }

    @Override
    public SimpleResponse updateWorkSpaceById(Long id, WorkSpaceRequest workSpaceRequest) {
        User user = jwtService.getAuthentication();
        WorkSpace workSpace = workSpaceRepository.getWorkSpaceByAdminIdAndId(user.getId(), id)
                .orElseThrow(() -> {
                    log.error("WorkSpace with id " + id + " not found ! ");
                    return new NotFoundException("WorkSpace with id " + id + " not found ! ");
                });
        workSpace.setName(workSpaceRequest.getName());
        workSpaceRepository.save(workSpace);
        log.info(String.format("WorkSpace with id %s  successfully updated !", id));
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("WorkSpace with id %s  successfully updated !", id))
                .build();
    }

    @Override
    public SimpleResponse deleteWorkSpaceById(Long id) {
        User user = jwtService.getAuthentication();
        WorkSpace workSpace = workSpaceRepository.getWorkSpaceByAdminIdAndId(user.getId(), id)
                .orElseThrow(() -> {
                    log.error("WorkSpace with id " + id + " not found ! ");
                    return new NotFoundException("WorkSpace with id " + id + " not found ! ");
                });
        workSpaceRepository.delete(workSpace);
        log.info(String.format("WorkSpace with id %s  successfully deleted !", id));
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("WorkSpace with id %s  successfully deleted !", id))
                .build();
    }
}

