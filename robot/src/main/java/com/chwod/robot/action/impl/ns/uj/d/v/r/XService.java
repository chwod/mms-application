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
			List<String> subjectList = new ArrayList<>();
			subjectList.add(partList.get(2).getWord());
			eventContext.setSubject(subjectList);
			eventContext.setPredicate(partList.get(3).getWord());
			List<String> attributiveList = new ArrayList<>();
			attributiveList.add(partList.get(0).getWord());
			eventContext.setAttributiveBeforeSubject(attributiveList);
			eventContext.setFocus(EventContext.SENTENCE_PART_OF_SPEECH_SUBJECT);
			
			return sentence;
		}

		// only one solution or one highest possible solution
		Knowledge maxPossibleSolution = solutions.get(0);
		if ((solutions.size() == 1) || (solutions.get(1).getCount() * 2 <= maxPossibleSolution.getCount())) {
			String result = solutions.get(0).getName();
			sentence.setResponseWord(result);
			
			// context setup
			eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
			List<String> subjectList = new ArrayList<>();
			subjectList.add(partList.get(2).getWord());
			eventContext.setSubject(subjectList);
			eventContext.setPredicate(partList.get(3).getWord());
			List<String> attributiveList = new ArrayList<>();
			attributiveList.add(partList.get(0).getWord());
			eventContext.setAttributiveBeforeSubject(attributiveList);
			List<String> objectList = new ArrayList<>();
			objectList.add(result);
			eventContext.setObject(objectList);
			eventContext.setFocus(EventContext.SENTENCE_PART_OF_SPEECH_OBJECT);
			
			return sentence;
		}

		// more than one highest possible solutions.
		StringBuffer responseWord = new StringBuffer("可能是");
		List<String> subjectList = new ArrayList<>();
		subjectList.add(maxPossibleSolution.getName());
		responseWord.append(maxPossibleSolution.getName()).append(",也可能是");

		for (int i = 1; i < solutions.size(); i++) {
			Knowledge knowledge = solutions.get(i);
			if (knowledge.getCount() * 2 >= maxPossibleSolution.getCount()) {
				responseWord.append(knowledge.getName());
				responseWord.append("、");
				subjectList.add(knowledge.getName());
				continue;
			}
			break;
		}
		responseWord.deleteCharAt(responseWord.length() - 1);
		responseWord.replace(responseWord.lastIndexOf("、"), responseWord.lastIndexOf("、") + 1, "，或者是");
		responseWord.append("。");
		sentence.setResponseWord(responseWord.toString());
		
		// context setup
		eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
		eventContext.setSubject(subjectList);
		eventContext.setPredicate(EventContext.SENTECNE_PREDICATE_TYPE_IS);
		List<String> objectList = new ArrayList<>();
		objectList.add(partList.get(2).getWord());
		eventContext.setObject(objectList);
		eventContext.setFocus(EventContext.SENTENCE_PART_OF_SPEECH_SUBJECT);
		
		return sentence;
	}

	@Override
	public void learning(EventContext eventContext, LEARNING flag) {
		logger.debug("Learning class : [{}], sentence : [{}]", this.getClass().getName(),
				eventContext.getCurrentEvent());
	}
}
