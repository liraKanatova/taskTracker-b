package peaksoft.house.tasktrackerb9.services.impl;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.WorkSpaceRequest;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceInnerPageResponse;
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

import java.util.ArrayList;
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
    private final JdbcTemplate jdbcTemplate;
    private final WorkSpaceJdbcTemplateService workSpaceJdbcTemplateService;

    @Override
    public List<WorkSpaceResponse> getAllWorkSpaces() {
        User user = jwtService.getAuthentication();
        String getAllWorkSpaces = workSpaceJdbcTemplateService.getAllWorkSpaces();
        List<WorkSpaceResponse> workSpaceResponses = jdbcTemplate.query(getAllWorkSpaces,
                new Object[]{user.getId()}, (rs, rowNum) -> {
                    WorkSpaceResponse workSpaceResponse = new WorkSpaceResponse();
                    workSpaceResponse.setId(rs.getLong("id"));
                    workSpaceResponse.setName(rs.getString("workSpaceName"));
                    workSpaceResponse.setAdminId(rs.getLong("userId"));
                    workSpaceResponse.setFullName(rs.getString("fullName"));
                    workSpaceResponse.setImage(rs.getString("image"));
                    return workSpaceResponse;
                }
        );
        return workSpaceResponses;
    }


    @Override
    public SimpleResponse saveWorkSpace(WorkSpaceRequest request) throws MessagingException {
        User user = jwtService.getAuthentication();
        WorkSpace workspace = new WorkSpace();
        workspace.setName(request.getName());
        workspace.setAdminId(user.getId());
        UserWorkSpaceRole userWorkSpace = new UserWorkSpaceRole();
        userWorkSpace.setUser(user);
        userWorkSpace.setWorkSpace(workspace);
        userWorkSpace.setRole(Role.ADMIN);
        user.setRoles(List.of(userWorkSpace));
        workspace.setRoles(List.of(userWorkSpace));
        user.setWorkSpaces(List.of(workspace));
        workspace.setUsers(List.of(user));
        workSpaceRepository.save(workspace);
        userWorkSpaceRoleRepository.save(userWorkSpace);
        List<String> invitationEmails = request.getEmails();
        if (!invitationEmails.isEmpty() && !invitationEmails.get(0).isBlank()) {
            for (String email : invitationEmails) {
                if (!userRepository.existsByEmail(email)) {
                    String inviteLink = "http://localhost:8080/swagger-ui/index.html#/invite-registration-api/registerUser";
                    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    helper.setSubject(" Welcome to my workspace");
                    helper.setFrom("abduvohobuulumatmusa@gmail.com");
                    helper.setTo(email);
                    helper.setText("/workspaceId/" + workspace.getId() + "   " + inviteLink);
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
    public WorkSpaceInnerPageResponse getWorkSpaceById(Long id) {
        User user = jwtService.getAuthentication();
        String getWorkSpaceInnerPage = workSpaceJdbcTemplateService.getAllWorkSpaceInnerPage();
        WorkSpaceInnerPageResponse workSpaceInnerPageResponse = jdbcTemplate.query(getWorkSpaceInnerPage, new Object[]{user.getId(), id}, (rs) -> {
            WorkSpaceResponse workSpaceResponse = new WorkSpaceResponse();
            List<BoardResponse> boardResponses = new ArrayList<>();
            while (rs.next()) {
                if (workSpaceResponse.getId() == null) {
                    workSpaceResponse.setId(rs.getLong("id"));
                    workSpaceResponse.setName(rs.getString("workSpaceName"));
                    workSpaceResponse.setAdminId(rs.getLong("userId"));
                    workSpaceResponse.setFullName(rs.getString("fullName"));
                    workSpaceResponse.setImage(rs.getString("image"));
                }
                BoardResponse boardResponse = new BoardResponse();
                boardResponse.setId(rs.getLong("boardId"));
                boardResponse.setTitle(rs.getString("title"));
                boardResponse.setBackGround(rs.getString("back_ground"));
                boardResponses.add(boardResponse);
            }
            WorkSpaceInnerPageResponse workSpaceInnerPageResponse1 = new WorkSpaceInnerPageResponse();
            workSpaceInnerPageResponse1.setWorkSpaceResponse(workSpaceResponse);
            workSpaceInnerPageResponse1.setBoardResponses(boardResponses);
            return workSpaceInnerPageResponse1;
        });

        return workSpaceInnerPageResponse;
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

