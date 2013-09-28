package net.chunk64.french;

import java.util.Scanner;

public class Game implements Runnable
{

	private boolean running = false;
	public static StatsManager stats = new StatsManager();

	private Scanner scanner = new Scanner(System.in);

	public void start()
	{
		running = true;

		French.log("Game started");
		stats.setStartTime();

		Thread thread = new Thread(this);
		thread.start();
	}

	public void stop()
	{
		running = false;
		stats.setFinishTime();
		French.log("Game finished");
		scanner.close();

		stats.viewStats();

	}

	@Override
	public void run()
	{
		String format = "%d: Translate \"%s\" to %s\n  > ";
		String question, input, answer;
		boolean correct;
		Vocab vocab;
		while (running)
		{
			// Send question
			question = Vocab.nextQuestion();
			vocab = Vocab.getQuestionVocab();
			answer = vocab.getAnswer(question);
			French.print(String.format(format, stats.getQuestionNumber(), question, answer.equals(vocab.getEnglish()) ? "english" : "french"));

			// Get answer
			input = scanner.nextLine();

			// Exit?
			if (input.equalsIgnoreCase("Q"))
				break;

			// Check answer
			correct = vocab.isCorrect(question, input);

			French.log((correct ? "Well done, you're correct!" : "Ah crap, you're wrong! The answer was \"" + vocab.getAnswer(question) + "\"") + "\n");

			stats.increment(correct);

		}

		stop();
	}

}
