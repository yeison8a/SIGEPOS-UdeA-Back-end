package edu.udea.sigepos.service;

import edu.udea.sigepos.model.Document;
import edu.udea.sigepos.model.User;
import edu.udea.sigepos.repository.DocumentRepository;
import edu.udea.sigepos.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public DocumentService(DocumentRepository documentRepository,
                           UserRepository userRepository,
                           S3Service s3Service) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    public Document subirDocumento(UUID userId, MultipartFile archivo) throws IOException {
        User usuario = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        String url = s3Service.subirArchivo(archivo);

        Document doc = Document.builder()
                .nombre(archivo.getOriginalFilename())
                .url(url)
                .fechaSubida(LocalDateTime.now())
                .usuario(usuario)
                .build();

        return documentRepository.save(doc);
    }

    public List<Document> listarDocumentosPorUsuario(UUID userId) {
        return documentRepository.findByUsuarioId(userId);
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
