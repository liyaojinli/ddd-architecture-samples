package dev.huhao.samples.ddd.blogservice.adapters.inbound.grpc.draft;

import dev.huhao.samples.ddd.blogservice.adapters.inbound.grpc.GrpcServiceIntegrationTestBase;
import dev.huhao.samples.ddd.blogservice.adapters.inbound.grpc.draft.proto.CreateDraftRequest;
import dev.huhao.samples.ddd.blogservice.adapters.inbound.grpc.draft.proto.DraftDto;
import dev.huhao.samples.ddd.blogservice.adapters.inbound.grpc.draft.proto.DraftServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DraftGrpcServiceTest extends GrpcServiceIntegrationTestBase {
    @GrpcClient("inProcess")
    private DraftServiceGrpc.DraftServiceBlockingStub draftGrpcService;

    @Nested
    class createDraft {

        @Test
        @DirtiesContext
        void should_return_created_dto() {
            // Given
            CreateDraftRequest request = CreateDraftRequest.newBuilder()
                    .setTitle("Hello")
                    .setBody("A Nice Day...")
                    .setAuthorId(UUID.randomUUID().toString())
                    .build();

            // When
            DraftDto draftDto = draftGrpcService.createDraft(request);

            // Then
            assertThat(draftDto.getBlogId()).isNotNull();
            assertThat(draftDto.getTitle()).isEqualTo(request.getTitle());
            assertThat(draftDto.getBody()).isEqualTo(request.getBody());
            assertThat(draftDto.getAuthorId()).isEqualTo(request.getAuthorId());
            assertThat(draftDto.getCreatedAt()).isNotBlank();
            assertThat(draftDto.getSavedAt()).isEqualTo(draftDto.getCreatedAt());
        }

        @Test
        void should_return_error_when_not_have_authorId() {

            CreateDraftRequest request = CreateDraftRequest.newBuilder()
                    .setTitle("Hello")
                    .setBody("A Nice Day...")
                    .setAuthorId("")
                    .build();

            assertThatThrownBy(() -> draftGrpcService.createDraft(request))
                    .isInstanceOf(StatusRuntimeException.class)
                    .hasMessage(Status.INVALID_ARGUMENT.withDescription("the blog must have author")
                            .asRuntimeException().getMessage());
        }
    }
}
