package com.codestates.reviewBoard.controller;

import com.codestates.dto.ResponseDto;
import com.codestates.reviewBoard.dto.ReviewBoardDto;
import com.codestates.reviewBoard.entity.ReviewBoard;
import com.codestates.reviewBoard.mapper.ReviewBoardMapper;
import com.codestates.reviewBoard.service.ReviewBoardService;
import com.codestates.utils.UriComponent;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RequestMapping("/reviewBoards")
@RestController
@Validated
public class ReviewBoardController {
    private final static String REVIEWBOARD_DEFAULT_URI = "/reviewBoards";
    private final ReviewBoardService reviewBoardService;
    private final ReviewBoardMapper mapper;

    public ReviewBoardController(ReviewBoardService reviewBoardService, ReviewBoardMapper mapper) {
        this.reviewBoardService = reviewBoardService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postReviewBoard(@Positive long userId,
                                          @Valid @RequestBody ReviewBoardDto.Post post) {
        ReviewBoard reviewBoard = reviewBoardService.createReviewBoard(userId, mapper.PostToReviewBoard(post));
        URI location = UriComponent.createUri(REVIEWBOARD_DEFAULT_URI, reviewBoard.getReviewBoardId());
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    @PatchMapping("/{review-id}")
    public ResponseEntity patchReviewBoard(@Positive long userId,
                                           @PathVariable("review-id") @Positive long reviewId,
                                           @Valid @RequestBody ReviewBoardDto.Patch patch) {
        patch.setReviewBoardId(reviewId);
        ReviewBoard reviewBoard = reviewBoardService.updateReviewBoard(userId, mapper.PatchToReviewBoard(patch));
        ReviewBoardDto.Response response = mapper.reviewBoardToResponse(reviewBoard);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{review-id}")
    public ResponseEntity getReviewBoard(@PathVariable("review-id") @Positive long reviewId) {
        ReviewBoard reviewBoard = reviewBoardService.findReviewBoard(reviewId);
        return new ResponseEntity<>(mapper.reviewBoardToResponse(reviewBoard), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAllReviewBoards(@Positive @RequestParam int page,
                                             @Positive @RequestParam int size) {
        Page<ReviewBoard> pageReviewBoards = reviewBoardService.findReviewBoards(page-1,size);
        List<ReviewBoard> reviewBoards = pageReviewBoards.getContent();

        return new ResponseEntity<>(new ResponseDto.MultipleResponseDto<>(mapper.reviewBoardsToResponses(reviewBoards), pageReviewBoards), HttpStatus.OK);
    }

    @DeleteMapping("/{review-id}")
    public ResponseEntity deleteReviewBoard(@PathVariable("review-id") @Positive long reviewId) {
        reviewBoardService.deleteReviewBoard(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}