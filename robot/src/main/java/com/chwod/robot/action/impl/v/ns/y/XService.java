package com.chwod.robot.action.impl.v.ns.y;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.domain.Knowledge;
import com.chwod.robot.service.KnowledgeService;
import com.chwod.robot.utils.Constants;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec	知道北京吗？
 * @author liuxun
 *
 */
@Component("SS:V-NS-Y-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Autowired
	private KnowledgeService knowledgeService;
	
	@Override
	public Sentence process(Sentence sentence) {
		
		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(), sentence.getRequestWord(),
				sentence.getProcessDeep());
		
		List<Knowledge> knowledgeList = this.knowledgeService.getKnowSolutions(sentence.getPartList().get(1).getWord(),
				Constants.IS);

		// unknown
		if (knowledgeList == null || knowledgeList.size() <= 0) {
			sentence.setResponseWord("不知道");
			return sentence;
		}

		// one solution
		if (knowledgeList.size() == 1) {
			sentence.setResponseWord(new StringBuffer("知道,").append(sentence.getPartList().get(1).getWord()).append("是")
					.append(knowledgeList.get(0).getContent()).toString());
			return sentence;
		}
		
		//more than one solution
		StringBuffer responseWords = new StringBuffer("知道,").append(sentence.getPartList().get(1).getWord()).append("是");
		for(int i = 0; i < knowledgeList.size(); i++) {
			responseWords.append(knowledgeList.get(i).getContent());
			if(i < knowledgeList.size() - 1) {
				responseWords.append(", 也是");
			}
		}
		responseWords.append("。");
		sentence.setResponseWord(responseWords.toString());
		return sentence;
	}

	@Override
	public void learning(Sentence sentence, LEARNING flag) {
		// TODO Auto-generated method stub
		
	}

}
