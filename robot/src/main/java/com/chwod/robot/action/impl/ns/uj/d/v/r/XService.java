package com.chwod.robot.action.impl.ns.uj.d.v.r;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
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
	public Sentence process(Sentence sentence) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(), sentence.getRequestWord(),
				sentence.getProcessDeep());
		
		List<Part> partList = sentence.getPartList();

		List<Knowledge> solutions = this.knowledgeService.GetAllPossibleNameSolutions(partList.get(2).getWord(),
				partList.get(0).getWord());
		
		// no solution
		if(solutions == null || solutions.size() <= 0) {
			sentence.setResponseWord("不知道");
			return sentence;
		}
		
		// only one solution
		if(solutions.size() == 1) {
			sentence.setResponseWord(solutions.get(0).getName());
			return sentence;
		}
		
		// more than one solutions
		solutions.sort(new Comparator<Knowledge>() {

			@Override
			public int compare(Knowledge v1, Knowledge v2) {
				if(v1.getCount() > v2.getCount()) {
					return 1;
				}
				if(v1.getCount() < v2.getCount()) {
					return -1;
				}
				return 0;
			}
		});
		
		Knowledge maxPossibleSolution = solutions.get(0);
		
		// only one highest possible solution.
		if(solutions.get(1).getCount() * 2 < maxPossibleSolution.getCount()) {
			sentence.setResponseWord(maxPossibleSolution.getName());
			return sentence;
		}
		
		// more than one highest possible solutions.
		StringBuffer responseWord = new StringBuffer("可能是");
		responseWord.append(maxPossibleSolution.getName()).append(",也可能是");
		
		for(int i = 1; i < solutions.size(); i++) {
			Knowledge knowledge = solutions.get(i);
			if(knowledge.getCount() * 2 >= maxPossibleSolution.getCount()) {
				responseWord.append(knowledge.getName());
				responseWord.append("、");
				continue;
			}
			break;
		}
		responseWord.deleteCharAt(responseWord.length() - 1);
		responseWord.replace(responseWord.lastIndexOf("、"), responseWord.lastIndexOf("、") + 1, "，或者是");
		responseWord.append("。");
		
		sentence.setResponseWord(responseWord.toString());
		return sentence;
	}

	@Override
	public void learning(Sentence sentence, LEARNING flag) {
		// TODO Auto-generated method stub
		
	}
}
