package com.chwod.robot.action;

import com.chwod.robot.bean.Sentence;
import com.chwod.robot.utils.Constants;

public interface ActionService {

	/**
	 * send the sentence to the support service. and get the response word.
	 * @param sentence
	 * @return
	 */
	public Sentence process(Sentence sentence);
	
	/**
	 * robot learning with learning flag.
	 * @param sentence
	 * @param flag
	 */
	public void learning(Sentence sentence, Constants.LEARNING flag);
}
