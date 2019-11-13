package com.chwod.robot.action.impl.ns.v.ns.uj;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.Part;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.domain.Knowledge;
import com.chwod.robot.service.KnowledgeService;
import com.chwod.robot.utils.Constants;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec	北京是中国的首都
 */

@Component("SS:NS-V-NS-UJ-D")
public class DService implements ActionService {
	
	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);
	
	@Autowired
	private KnowledgeService knowledgeService;

	@Override
	public Sentence process(Sentence sentence) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(), sentence.getRequestWord(),
				sentence.getProcessDeep());
		
		List<Part> partList = sentence.getPartList();
		
		String uuid = UUID.randomUUID().toString();
		
		Knowledge knowledge = new Knowledge();
		knowledge.setEventId(uuid);
		knowledge.setName(partList.get(0).getWord());
		knowledge.setProperty(Constants.IS);
		knowledge.setContent(partList.get(4).getWord());
		this.knowledgeService.save(knowledge);
		
		knowledge = new Knowledge();
		knowledge.setEventId(uuid);
		knowledge.setName(partList.get(0).getWord());
		knowledge.setProperty(Constants.BELONG);
		knowledge.setContent(partList.get(2).getWord());
		this.knowledgeService.save(knowledge);
		
		knowledge = new Knowledge();
		knowledge.setEventId(uuid);
		knowledge.setName(partList.get(4).getWord());
		knowledge.setProperty(Constants.BELONG);
		knowledge.setContent(partList.get(2).getWord());
		this.knowledgeService.save(knowledge);
		
		sentence.setResponseWord("OK");
		
		return sentence;
	}

	@Override
	public void learning(Sentence sentence, LEARNING flag) {
		// TODO Auto-generated method stub
		
	}

}
