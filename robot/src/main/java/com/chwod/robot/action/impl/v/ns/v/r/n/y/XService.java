package com.chwod.robot.action.impl.v.ns.v.r.n.y;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.action.impl.d.v.ns.ULService;
import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.domain.Knowledge;
import com.chwod.robot.service.KnowledgeService;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec	知道北京是什么地方吗？
 * @author liuxun
 *
 */
@Component("SS:V-NS-V-R-N-Y-X")
public class XService implements ActionService {
	
	private static final Logger logger = LoggerFactory.getLogger(ULService.class);

	@Autowired
	private KnowledgeService knowledgeService;
	
	@Override
	public Sentence process(Sentence sentence, EventContext eventContext) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(),
				sentence.getRequestWord(), sentence.getProcessDeep());

		String subject = sentence.getPartList().get(1).getWord();
		eventContext.setSubject(subject);

		List<Knowledge> knowledgeList = this.knowledgeService.getKnowSolutions(sentence.getPartList().get(1).getWord(),
				EventContext.SENTECNE_PREDICATE_TYPE_IS);

		// unknown
		if (knowledgeList == null || knowledgeList.size() <= 0) {
			// context setup
			eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE_NEGATIVE);
			sentence.setResponseWord("不知道");
			return sentence;
		}

		// one solution
		if (knowledgeList.size() == 1) {

			String object = knowledgeList.get(0).getContent();

			// context setup
			eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
			eventContext.setPredicate(EventContext.SENTECNE_PREDICATE_TYPE_IS);
			eventContext.setObject(object);

			sentence.setResponseWord(new StringBuffer("知道,").append(sentence.getPartList().get(1).getWord()).append("是")
					.append(object).toString());
			return sentence;
		}

		// more than one solution
		eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
		eventContext.setPredicate(EventContext.SENTECNE_PREDICATE_TYPE_IS);

		List<String> elementList = new ArrayList<>();
		elementList.add(subject);
		StringBuffer responseWords = new StringBuffer("知道,").append(subject).append("是");
		for (int i = 0; i < knowledgeList.size(); i++) {
			String content = knowledgeList.get(i).getContent();
			responseWords.append(content);
			elementList.add(content);
			if (i < knowledgeList.size() - 1) {
				responseWords.append(", 也是");
			}
		}
		responseWords.append("。");

		eventContext.setElementList(elementList);
		sentence.setResponseWord(responseWords.toString());
		return sentence;
	}

	@Override
	public void learning(EventContext eventContext, LEARNING flag) {
		// TODO Auto-generated method stub

	}

}
