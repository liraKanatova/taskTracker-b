package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.ChangeRoleRequest;
import peaksoft.house.tasktrackerb9.dto.request.InviteRequest;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.models.*;
import peaksoft.house.tasktrackerb9.repositories.*;
import peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl.CustomMemberRepositoryImpl;
import static org.mockito.ArgumentMatchers.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DataJpaTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class MemberServiceImplTest {

   @InjectMocks
    private MemberServiceImpl memberService;
    @Mock
    private WorkSpaceRepository workSpaceRepository;
    @Mock
    private CustomMemberRepositoryImpl customMemberRepository;
    @Autowired
    private CardRepository cardRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private UserWorkSpaceRoleRepository userWorkSpaceRoleRepository;

    @BeforeEach
    void setUp() {
        memberService = new MemberServiceImpl(
                userWorkSpaceRoleRepository,
                boardRepository,
                userRepository,
                jwtService,
                workSpaceRepository,
                cardRepository,
                customMemberRepository,
                javaMailSender
        );
    }

    @Test
    void searchByEmail() {
        Long workSpaceId = 1L;
        String email = "test@example.com";
        WorkSpace workSpace = new WorkSpace();
        when(workSpaceRepository.findById(workSpaceId)).thenReturn(Optional.of(workSpace));
        List<MemberResponse> expectedMembers = new ArrayList<>();
        MemberResponse member1 = new MemberResponse(1L, "Abdumalik", "Turatbek uulu", "asanbekovmalik2@gmail.com", "image1.jpg", Role.ADMIN);
        MemberResponse member2 = new MemberResponse(2L, "Manas", "Abdugani uulu", "manas@gmail.com", "image2.jpg", Role.ADMIN);
        expectedMembers.add(member1);
        expectedMembers.add(member2);
        when(customMemberRepository.searchByEmail(workSpace.getId(), email)).thenReturn(expectedMembers);
        List<MemberResponse> actualMembers = memberService.searchByEmail(workSpaceId, email);
        assertEquals(expectedMembers, actualMembers);
        Mockito.verify(workSpaceRepository, times(1)).findById(workSpaceId);
        Mockito.verify(customMemberRepository, times(1)).searchByEmail(workSpace.getId(), email);
    }

    @Test
    void inviteMemberToBoard() throws MessagingException {
        InviteRequest request = new InviteRequest();
        request.setBoardId(1L);
        request.setEmail("test@example.com");
        request.setRole(Role.MEMBER);
        request.setLink("example.com/board/1");

        WorkSpace workSpace = new WorkSpace();
        workSpace.setId(1L);

        Board board = new Board();
        board.setId(1L);
        board.setWorkSpace(workSpace);

        if (board.getMembers() == null) {
            board.setMembers(new ArrayList<>());
        }
        User user = new User();
        user.setEmail(request.getEmail());

        when(boardRepository.findById(request.getBoardId())).thenReturn(Optional.of(board));
        when(workSpaceRepository.findById(board.getWorkSpace().getId())).thenReturn(Optional.of(workSpace));
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(user));

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        SimpleResponse response = memberService.inviteMemberToBoard(request);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Message sent successfully!", response.getMessage());

        verify(boardRepository).findById(request.getBoardId());
        verify(workSpaceRepository).findById(board.getWorkSpace().getId());
        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository).findUserByEmail(request.getEmail());
        verify(userWorkSpaceRoleRepository).save(any(UserWorkSpaceRole.class));

        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    void changeMemberRole() {
        Long adminId = 1L;
        Long memberId = 2L;
        Long boardId = 3L;

        User admin = new User();
        admin.setId(adminId);

        User member = new User();
        member.setId(memberId);

        WorkSpace workSpace = new WorkSpace();
        workSpace.setId(4L);
        workSpace.setAdminId(adminId);

        Board board = new Board();
        board.setId(boardId);
        board.setWorkSpace(workSpace);

        List<Board> boards = new ArrayList<>();
        boards.add(board);
        workSpace.setBoards(boards);

        UserWorkSpaceRole userWorkSpaceRole = new UserWorkSpaceRole();
        userWorkSpaceRole.setMember(member);
        userWorkSpaceRole.setRole(Role.MEMBER);
        userWorkSpaceRole.setWorkSpace(workSpace);

        when(jwtService.getAuthentication()).thenReturn(admin);
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(userRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(userWorkSpaceRoleRepository.findByUserId(boardId, memberId))
                .thenReturn(Collections.singletonList(userWorkSpaceRole));

        ChangeRoleRequest request = ChangeRoleRequest.builder()
                .memberId(memberId)
                .boardId(boardId)
                .role(Role.MEMBER)
                .build();
        SimpleResponse response = memberService.changeMemberRole(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Member role changed successfully!", response.getMessage());
    }

    @Test
    void getAllMembersFromBoard() {
        Long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        board.setTitle("board1");
        board.setBackGround("red");

        List<MemberResponse> expectedMembers = new ArrayList<>();
        expectedMembers.add(new MemberResponse(1L, "Abdumalik", "Turatbek uulu", "asanbekovmalik2@gmail.com", "image1.jpg", Role.ADMIN));
        expectedMembers.add(new MemberResponse(2L, "Manas", "Abdugani uulu", "manas@gmail.com", "image2.jpg", Role.ADMIN));

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(customMemberRepository.getAllMembersFromBoard(boardId)).thenReturn(expectedMembers);

        List<MemberResponse> actualMembers = memberService.getAllMembersFromBoard(boardId);

        assertEquals(expectedMembers.size(), actualMembers.size());
        for (int i = 0; i < expectedMembers.size(); i++) {
            MemberResponse expected = expectedMembers.get(i);
            MemberResponse actual = actualMembers.get(i);
            assertEquals(expected.getUserId(), actual.getUserId());
            assertEquals(expected.getFirstName(), actual.getFirstName());
            assertEquals(expected.getLastName(), actual.getLastName());
            assertEquals(expected.getEmail(), actual.getEmail());
            assertEquals(expected.getImage(), actual.getImage());
            assertEquals(expected.getRole(), actual.getRole());
        }
        verify(boardRepository, times(1)).findById(boardId);
        verify(customMemberRepository, times(1)).getAllMembersFromBoard(boardId);
    }

    @Test
    void removeMemberFromBoard() {
        User admin = new User();
        admin.setId(1L);
        User member = new User();
        member.setId(2L);
        Board board = new Board();
        board.setId(3L);
        board.setTitle("board1");
        board.setBackGround("red");
        board.setMembers(new ArrayList<>());

        board.setWorkSpace(new WorkSpace());
        board.getWorkSpace().setAdminId(admin.getId());
        UserWorkSpaceRole workSpaceRole = new UserWorkSpaceRole();
        workSpaceRole.setMember(member);

        workSpaceRole.setWorkSpace(board.getWorkSpace());
        workSpaceRole.getWorkSpace().setBoards(Collections.singletonList(board));

        when(jwtService.getAuthentication()).thenReturn(admin);
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(userRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(userWorkSpaceRoleRepository.findByUserId(board.getId(), member.getId())).thenReturn(Collections.singletonList(workSpaceRole));

        SimpleResponse result = memberService.removeMemberFromBoard(member.getId(), board.getId());

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Member removed from board successfully!", result.getMessage());

        verify(jwtService, times(1)).getAuthentication();
        verify(boardRepository, times(1)).findById(board.getId());
        verify(userRepository, times(1)).findById(member.getId());
        verify(userWorkSpaceRoleRepository, times(1)).findByUserId(board.getId(), member.getId());
        verify(userWorkSpaceRoleRepository, times(1)).delete(workSpaceRole);
  }
}