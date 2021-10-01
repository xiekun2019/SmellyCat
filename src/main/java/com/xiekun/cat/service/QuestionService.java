package com.xiekun.cat.service;

import com.xiekun.cat.dto.QuestionDTO;
import com.xiekun.cat.mapper.QuestionMapper;
import com.xiekun.cat.mapper.UserMapper;
import com.xiekun.cat.model.Question;
import com.xiekun.cat.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public List<QuestionDTO> getList() {
        List<Question> questions = questionMapper.getList();
        ArrayList<QuestionDTO> questionDTOArrayList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOArrayList.add(questionDTO);
        }
        return questionDTOArrayList;
    }
}
