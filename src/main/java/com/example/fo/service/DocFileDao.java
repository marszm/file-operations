package com.example.fo.service;

import com.example.fo.model.FileDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocFileDao extends CrudRepository<FileDocument, Long> {
    FileDocument findByFileName(String fileName);
}
