package com.chwod.robot.action.impl.ns.uj.d.v.r;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Part;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.domain.Knowledge;
import com.chwod.robot.service.KnowledgeService;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 中国的首都是哪里？
 */

@Component("SS:NS-UJ-D-V-R-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Autowired
	private KnowledgeService knowledgeService;

	@Override
	public Sentence process(Sentence sentence, EventContext eventContext) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(),
				sentence.getRequestWord(), sentence.getProcessDeep());

		List<Part> partList = sentence.getPartList();

		List<Knowledge> solutions = this.knowledgeService.GetAllPossibleNameSolutions(partList.get(2).getWord(),
				partList.get(0).getWord());

		// no solution
		if (solutions == null || solutions.size() <= 0) {
			sentence.setResponseWord("不知道");
			
			// context setup
			eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE_NEGATIVE);
			eventContext.setSubject(partList.get(2).getWord());
			eventContext.setPredicate(partList.get(3).getWord());
			eventContext.setAttributive(partList.get(0).getWord());
			
			return sentence;
		}

		// only one solution or one highest possible solution
		Knowledge maxPossibleSolution = solutions.get(0);
		if ((solutions.size() == 1) || (solutions.get(1).getCount() * 2 < maxPossibleSolution.getCount())) {
			String name = solutions.get(0).getName();
			sentence.setResponseWord(name);
			
			// context setup
			eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
			eventContext.setSubject(partList.get(2).getWord());
			eventContext.setPredicate(partList.get(3).getWord());
			eventContext.setAttributive(partList.get(0).getWord());
			eventContext.setObject(name);
			eventContext.setFocus(name);
			
			return sentence;
		}

		// more than one highest possible solutions.
		StringBuffer responseWord = new StringBuffer("可能是");
		List<String> elementList = new ArrayList<>();
		elementList.add(maxPossibleSolution.getName());
		responseWord.append(maxPossibleSolution.getName()).append(",也可能是");

		for (int i = 1; i < solutions.size(); i++) {
			Knowledge knowledge = solutions.get(i);
			if (knowledge.getCount() * 2 >= maxPossibleSolution.getCount()) {
				responseWord.append(knowledge.getName());
				responseWord.append("、");
				elementList.add(knowledge.getName());
				continue;
			}
			break;
		}
		responseWord.deleteCharAt(responseWord.length() - 1);
		responseWord.replace(responseWord.lastIndexOf("、"), responseWord.lastIndexOf("、") + 1, "，或者是");
		responseWord.append("。");
		elementList.add(partList.get(2).getWord());
		sentence.setResponseWord(responseWord.toString());
		
		// context setup
		eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
		eventContext.setElementList(elementList);
		
		return sentence;
	}

	@Override
	public void learning(EventContext eventContext, LEARNING flag) {
		// TODO Auto-generated method stub

	}
}
