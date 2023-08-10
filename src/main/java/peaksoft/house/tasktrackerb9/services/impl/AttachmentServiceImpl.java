package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.AttachmentRequest;
import peaksoft.house.tasktrackerb9.dto.response.AttachmentResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Attachment;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.models.WorkSpace;
import peaksoft.house.tasktrackerb9.repositories.AttachmentRepository;
import peaksoft.house.tasktrackerb9.repositories.CardRepository;
import peaksoft.house.tasktrackerb9.repositories.WorkSpaceRepository;
import peaksoft.house.tasktrackerb9.services.AttachmentService;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final WorkSpaceRepository workSpaceRepository;
    private final CardRepository cardRepository;
    private final AttachmentRepository attachmentRepository;
    private final JwtService service;

    @Override
    public AttachmentResponse saveAttachmentToCard(AttachmentRequest attachmentRequest) {
        User user = service.getAuthentication();
        Card card = cardRepository.findById(attachmentRequest.cardId())
                .orElseThrow(() -> {
                    log.error("Card with id: " + attachmentRequest.cardId() + " not found");
                    return new NotFoundException("Card with id: " + attachmentRequest.cardId() + " not found");
                });
        WorkSpace workSpace = workSpaceRepository.findById(card.getColumn().getBoard().getWorkSpace().getId())
                .orElseThrow(() -> {
                    log.error("Workspace with id: " + card.getColumn().getBoard().getWorkSpace().getId() + " not found");
                    throw new NotFoundException("Workspace with id: " + card.getColumn().getBoard().getWorkSpace().getId() + " not found");
                });
        if(!workSpace.getMembers().contains(user)){
            throw new BadCredentialException("You are not member this workSpace");
        }
        Attachment attachment = new Attachment();
        attachment.setDocumentLink(attachmentRequest.documentLink());
        attachment.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Bishkek")));
        attachment.setCard(card);
        card.getAttachments().add(attachment);
        attachmentRepository.save(attachment);
        return AttachmentResponse.builder()
                .attachmentId(attachment.getId())
                .documentLink(attachment.getDocumentLink())
                .createdAt(attachment.getCreatedAt())
                .build();
    }

    @Override
    public SimpleResponse deleteAttachment(Long attachmentId) {

        User user = service.getAuthentication();
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> {
                    log.error("Attachment with id: " + attachmentId + " not found");
                    return new NotFoundException("Attachment with id: " + attachmentId + " not found");
                });
        WorkSpace workSpace = workSpaceRepository.findById(attachment.getCard().getColumn().getBoard().getWorkSpace().getId())
                .orElseThrow(() -> {
                    log.error("WorkSpace with id: " + attachment.getCard().getColumn().getBoard().getWorkSpace().getId() + " not found");
                    return new NotFoundException("WorkSpace with id: " + attachment.getCard().getColumn().getBoard().getWorkSpace().getId() + " not found");
                });
        if(!workSpace.getMembers().contains(user)){
            throw new BadCredentialException("You are not member this workSpace");
        }
        Card card = attachment.getCard();
        card.getAttachments().remove(attachment);
        attachmentRepository.deleteById(attachment.getId());
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Attachment deleted successfully")
                .build();
    }
}
