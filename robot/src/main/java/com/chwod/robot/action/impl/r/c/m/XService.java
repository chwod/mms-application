package com.chwod.robot.action.impl.r.c.m;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.domain.TalkLog;
import com.chwod.robot.service.TalkLogService;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec	你再说一遍？
 */

@Component("SS:R-C-M-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);
	
	@Autowired
	private TalkLogService talkLogService;

	@Override
	public Sentence process(Sentence sentence) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(), sentence.getRequestWord(),
				sentence.getProcessDeep());
		
		TalkLog talkLog = this.talkLogService.getLastTalkLogBySessionId(sentence.getSession().getId());
		sentence.setResponseWord(talkLog == null ? "我刚才什么也没有说啊！" : talkLog.getResponseWord());
		return sentence;
	}

	@Override
	public void learning(Sentence sentence, LEARNING flag) {
		// TODO Auto-generated method stub
		
	}

}
