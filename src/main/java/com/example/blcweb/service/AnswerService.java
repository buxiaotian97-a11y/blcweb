package com.example.blcweb.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.blcweb.dto.AnswerRequest;
import com.example.blcweb.dto.NextQuestionResponse;
import com.example.blcweb.dto.QuestionDto;
import com.example.blcweb.entity.Answer;
import com.example.blcweb.repository.AnswerRepository;
import com.example.blcweb.repository.QuestionRepository;

@Service
public class AnswerService {
  private final QuestionRepository qRepo;
  private final AnswerRepository aRepo;
  public AnswerService(QuestionRepository qRepo, AnswerRepository aRepo){ this.qRepo=qRepo; this.aRepo=aRepo; }

  public NextQuestionResponse submitAndNext(AnswerRequest req){
    if (req.runId()==null || req.runId().isBlank() || req.questionId()==null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid request");

    var q = qRepo.findById(req.questionId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "question not found"));

    int granted = req.answer()
    	    ? q.getYesPoint()
    	    : q.getNoPoint();
    
    aRepo.save(new Answer(req.runId(), q.getId(), req.answer(), granted));

    var next = qRepo.findFirstByIdGreaterThanOrderByIdAsc(q.getId()).orElse(null);
    return new NextQuestionResponse(next == null ? null : QuestionDto.from(next));
  }
}
