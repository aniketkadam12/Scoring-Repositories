package com.scoring.github.popularity_service.controller;

import com.scoring.github.popularity_service.dto.PopularityRequestDto;
import com.scoring.github.popularity_service.dto.PopularityResponseDto;
import com.scoring.github.popularity_service.service.RepositoryScoringService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repositories")
public class RepositoryScoringController {

    private final RepositoryScoringService repositoryScoringService;

    public RepositoryScoringController(RepositoryScoringService repositoryScoringService) {
        this.repositoryScoringService = repositoryScoringService;
    }

    @PostMapping("/popularity")
    public ResponseEntity<List<PopularityResponseDto>> getScore(
            @Valid @RequestBody PopularityRequestDto request) {

        List<PopularityResponseDto> result =
                repositoryScoringService.getRepositoriesByPopularity(
                        request.getLanguage(),
                        request.getCreatedAfter(),
                        request.getLimit(),
                        request.getAlgorithm()
                );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/supported-algorithms")
    public ResponseEntity<List<String>> getSupportedAlgorithms() {
        return ResponseEntity.ok(repositoryScoringService.getSupportedAlgorithms());
    }
}
