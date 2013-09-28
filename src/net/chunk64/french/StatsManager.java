package net.chunk64.french;

public class StatsManager
{
	private long startTime, finishTime;
	private int questionNumber = 1, correctAnswers = 0;

	/**
	 * Increments question number and correct answers if correct
	 */
	public void increment(boolean correct)
	{
		questionNumber++;
		if (correct)
			correctAnswers++;
	}

	public void setStartTime()
	{
		this.startTime = System.currentTimeMillis();
	}

	public void setFinishTime()
	{
		this.finishTime = System.currentTimeMillis();
	}

	public long getTotalTime()
	{
		return finishTime - startTime;
	}

	public int getQuestionNumber()
	{
		return questionNumber;
	}

	public void viewStats()
	{
		questionNumber--;

		System.out.println();
		French.log("--- STATS ---");
		French.log("You played for " + TimeParser.parseLong(getTotalTime(), true));
		French.log(String.format("You got %d/%d questions correct! That's %.1f%%!", correctAnswers, questionNumber, ((double) correctAnswers / (double) questionNumber) * 100));
		French.log("--- GAME OVER ---");
		System.exit(0);
	}
}
