package com.hebeu.ask.spider.service;

import com.hebeu.ask.dao.QuestionMapper;
import com.hebeu.ask.model.po.Question;
import com.hebeu.ask.model.po.QuestionExample;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : chenDeHua
 * Time   : 2018/4/22 下午12:13
 * Desc   :
 **/
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    /**
     * 通过文章标题判断是否存在文章
     * @param title 文章标题
     * @return 返回是否存在布尔值
     */
    public boolean isExistQuestionByTitle(String title) {
        QuestionExample questionExample = new QuestionExample();
        QuestionExample.Criteria criteria = questionExample.createCriteria();
        criteria.andTitleEqualTo(title);
        List<Question> questionList = questionMapper.selectByExample(questionExample);

        if (CollectionUtils.isEmpty(questionList)) {
            return false;
        }
        return true;

    }




}
