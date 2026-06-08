package com.sneha.doc_generator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sneha.doc_generator.model.Documentation;
import com.sneha.doc_generator.repository.DocumentationRepository;
import com.sneha.doc_generator.service.GeminiService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DocController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private DocumentationRepository documentationRepository;

    @PostMapping("/generate")
    public Documentation generateDocs(@RequestBody Documentation request) {

        String readme = geminiService.generateDocs(
            request.getCode(),
            request.getLanguage(),
            "readme"
        );

        String comments = geminiService.generateDocs(
            request.getCode(),
            request.getLanguage(),
            "comments"
        );

        String apidocs = geminiService.generateDocs(
            request.getCode(),
            request.getLanguage(),
            "apidocs"
        );

        Documentation doc = new Documentation();
        doc.setCode(request.getCode());
        doc.setLanguage(request.getLanguage());
        doc.setReadme(readme);
        doc.setComments(comments);
        doc.setApidocs(apidocs);

        return documentationRepository.save(doc);
    }
}
