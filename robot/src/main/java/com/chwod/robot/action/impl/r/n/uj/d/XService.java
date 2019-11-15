package com.chwod.robot.action.impl.r.n.uj.d;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.domain.Knowledge;
import com.chwod.robot.service.KnowledgeService;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 哪个国家的首都？
 * @author liuxun
 *
 */
@Component("SS:R-N-UJ-D-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Autowired
	private KnowledgeService knowledgeService;

	@Override
	public Sentence process(Sentence sentence, EventContext eventContext) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(),
				sentence.getRequestWord(), sentence.getProcessDeep());

		String subject = eventContext.getSubject();

		// no subject
		if (subject == null) {
			
			eventContext.setType(EventContext.SENTENCE_TYPE_QUESTION);
			
			sentence.setResponseWord(new StringBuffer("什么").append(sentence.getRequestWord()).toString());
			return sentence;
		}

		String modificationProperty = sentence.getPartList().get(1).getWord();

		// get all possible valid solutions
		List<Knowledge> knowledgeList = this.knowledgeService.getAllValidContentSolutions(subject,
				sentence.getPartList().get(3).getWord(), modificationProperty);
		// not found valid solutions.
		if (knowledgeList == null || knowledgeList.size() <= 0) {
			// get all possible content solutions.
			knowledgeList = this.knowledgeService.getAllPossibleContentSolutions(subject,
					sentence.getPartList().get(3).getWord());
			// not found
			if (knowledgeList == null || knowledgeList.size() <= 0) {
				sentence.setResponseWord("不知道");
				return sentence;
			}

			// one knowledge or one of most possible solution
			if (knowledgeList.size() == 1 || (knowledgeList.get(1).getCount() * 2 <= knowledgeList.get(0).getCount())) {

				String answer = knowledgeList.get(0).getContent();
				StringBuffer result = new StringBuffer(StringUtils.EMPTY);
				result.append(answer).append(",它是").append(modificationProperty).append("吗?");
				
				// setup context.
				eventContext.setType(EventContext.SENTENCE_TYPE_QUESTION_YESNO);
				eventContext.setCurrentEvent(sentence.getRequestWord());
				List<String> elementList = new ArrayList<>();
				elementList.add(answer);
				elementList.add(modificationProperty);
				eventContext.setElementList(elementList);
				
				sentence.setResponseWord(result.toString());
				return sentence;
			}

			// more solutions
			StringBuffer answer = new StringBuffer(StringUtils.EMPTY);
			List<String> elementList = new ArrayList<>();
			answer.append("最有可能的是").append(knowledgeList.get(0).getContent()).append("，但是还有可能是");
			for (int i = 1; i < knowledgeList.size(); i++) {
				Knowledge knowledge = knowledgeList.get(i);
				if (knowledge.getCount() * 2 > knowledgeList.get(0).getCount()) {
					answer.append(knowledge.getContent()).append("、");
					elementList.add(knowledge.getContent());
				}
			}
			answer.deleteCharAt(answer.length() - 1);
			answer.replace(answer.lastIndexOf("、"), answer.lastIndexOf("、") + 1, "，或者是");
			answer.append("，不过我不确定它们是不是").append(modificationProperty).append("，你能告诉我吗？");
			// setup context.
			eventContext.setType(EventContext.SENTENCE_TYPE_QUESTION_A_NOT_A);
			eventContext.setCurrentEvent(sentence.getRequestWord());
			elementList.add(modificationProperty);
			eventContext.setElementList(elementList);
			
			sentence.setResponseWord(answer.toString());
			return sentence;
		}

		// only one solution or one most possible solution.
		if ((knowledgeList.size() == 1) || (knowledgeList.get(1).getCount() * 2 <= knowledgeList.get(0).getCount())) {
			
			eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
			
			sentence.setResponseWord(knowledgeList.get(0).getContent());
			return sentence;
		}

		// more than one valid solutions.
		List<String> elementList = new ArrayList<>();
		StringBuffer answer = new StringBuffer(StringUtils.EMPTY);
		
		elementList.add(knowledgeList.get(0).getContent());
		answer.append("最有可能的是").append(knowledgeList.get(0).getContent()).append("，但是还有可能是");
		for (int i = 1; i < knowledgeList.size(); i++) {
			Knowledge knowledge = knowledgeList.get(i);
			if (knowledge.getCount() * 2 > knowledgeList.get(0).getCount()) {
				answer.append(knowledge.getContent()).append("、");
				elementList.add(knowledge.getContent());
				continue;
			}
			break;
		}
		answer.deleteCharAt(answer.length() - 1);
		answer.replace(answer.lastIndexOf("、"), answer.lastIndexOf("、") + 1, "，或者是");
		answer.append("。");
		elementList.add(modificationProperty);

		eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
		eventContext.setCurrentEvent(sentence.getRequestWord());
		eventContext.setElementList(elementList);
		
		sentence.setResponseWord(answer.toString());
		return sentence;
	}

	@Override
	public void learning(Sentence sentence, LEARNING flag) {
		// TODO Auto-generated method stub

	}

}
