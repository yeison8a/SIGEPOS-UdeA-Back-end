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

        String url = s3Service.subirArchivoConCarpeta(userId, archivo);

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

    public Document obtenerDocumento(UUID userId, UUID docId) {
        return documentRepository.findById(docId)
                .filter(doc -> doc.getUsuario().getId().equals(userId))
                .orElseThrow(() -> new DocumentNotFoundException("Documento no encontrado"));
    }

    public Document actualizarDocumento(UUID userId, UUID docId, MultipartFile archivo, String nuevoNombre) throws IOException {
        Document existing = obtenerDocumento(userId, docId);

        if (archivo != null && !archivo.isEmpty()) {
            s3Service.eliminarArchivoPorUrl(existing.getUrl());
            String nuevaUrl = s3Service.subirArchivoConCarpeta(userId, archivo);
            existing.setUrl(nuevaUrl);
            existing.setNombre(archivo.getOriginalFilename());
            existing.setFechaSubida(LocalDateTime.now());
        }

        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            existing.setNombre(nuevoNombre);
        }

        return documentRepository.save(existing);
    }

    public void eliminarDocumento(UUID userId, UUID docId) {
        Document existing = obtenerDocumento(userId, docId);
        s3Service.eliminarArchivoPorUrl(existing.getUrl());
        documentRepository.delete(existing);
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class DocumentNotFoundException extends RuntimeException {
        public DocumentNotFoundException(String message) {
            super(message);
        }
    }
}
