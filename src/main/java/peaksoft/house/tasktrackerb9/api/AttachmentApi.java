package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.AttachmentRequest;
import peaksoft.house.tasktrackerb9.dto.response.AttachmentResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.services.AttachmentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attachments")
@Tag(name = "Attachment Api", description = "Api methods for attachments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AttachmentApi {

    private final AttachmentService attachmentService;

    @PostMapping
    @Operation(summary = "Save attachment", description = "Save attachment by card id")
    public AttachmentResponse saveAttachment(@RequestBody AttachmentRequest attachmentRequest) {
        return attachmentService.saveAttachmentToCard(attachmentRequest);
    }

    @DeleteMapping("/{attachmentId}")
    @Operation(summary = "Delete attachment", description = "Delete attachment with id")
    public SimpleResponse deleteAttachment(@PathVariable Long attachmentId) {
        return attachmentService.deleteAttachment(attachmentId);
    }
}
