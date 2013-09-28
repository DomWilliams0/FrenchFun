package net.chunk64.french;

import java.util.HashSet;
import java.util.Random;

public class Vocab
{


	public static enum Gender
	{
		MASCULINE, FEMININE, NEUTRAL;

		public String toString()
		{
			return name().toLowerCase();
		}

		public static Gender getGender(String s)
		{
			for (Gender g : values())
				if (g.toString().toLowerCase().startsWith(s.toLowerCase()))
					return g;
			return null;
		}
	}

	private String french;
	private String english;
	private Gender gender;
	private long id;
	private boolean asked;
	private int askedCount;

	private static int totalAskedCount;
	private static long lastId = 0;
	private static long lastQuestionId;
	public static HashSet<Vocab> vocabList = new HashSet<Vocab>();
	public static final char[] VOWELS = "aàáâãäæeèéêëhiìíîïoòóôõöuùúûüùûúüœ".toCharArray();
	private static Random rand = new Random();

	private Vocab(String line)
	{

		String[] split = line.split(":");

		gender = split.length == 3 ? Gender.getGender(split[0]) : Gender.NEUTRAL;
		french = split[split.length == 3 ? 1 : 0].trim();
		english = split[split.length == 3 ? 2 : 1].trim();

		id = lastId++;
	}

	public static void createVocab(String line)
	{
		// Validate
		String[] split = line.split(":");
		if ((split.length != 3 || ((!split[0].equalsIgnoreCase("m") && !split[0].equalsIgnoreCase("f")))) && split.length != 2)
			return;

		Vocab v = new Vocab(line);
		vocabList.add(v);
	}


	public String getFrench() // Plural?
	{
		if (gender != Gender.NEUTRAL)
		{
			if (startsWithVowel(french))
			{
				return "l'" + french;
			}
			return (gender == Gender.MASCULINE ? "le " : "la ") + french;
		}
		return french;
	}

	public String getEnglish()
	{
		return english;
	}

	public Gender getGender()
	{
		return gender;
	}

	public long getId()
	{
		return id;
	}

	public boolean hasBeenAsked()
	{
		return asked;
	}

	public void setAsked(boolean asked)
	{
		this.asked = asked;
	}

	public int getAskedCount()
	{
		return askedCount;
	}

	public boolean isCorrect(String question, String input)
	{
		if (question.equalsIgnoreCase(getEnglish()))
			return input.trim().equalsIgnoreCase(getFrench().trim());
		if (question.equalsIgnoreCase(getFrench()))
			return input.trim().equalsIgnoreCase(getEnglish().trim());
		return false;
	}

	public String getAnswer(String question)
	{
		if (question.equalsIgnoreCase(getEnglish()))
			return getFrench().trim();
		if (question.equalsIgnoreCase(getFrench()))
			return getEnglish().trim();
		return null;
	}

	public static String nextQuestion()
	{

		boolean french = rand.nextBoolean();

		Vocab v = getRandom();

		// Refresh
		if (totalAskedCount >= vocabList.size() - rand.nextInt(vocabList.size() / 4 < 1 ? 1 : vocabList.size() / 4))
		{
			for (Vocab voc : vocabList)
				voc.setAsked(false);
			totalAskedCount = 0;
		}

		while (v.hasBeenAsked())
			v = getRandom();

		v.setAsked(true);
		v.askedCount++;
		totalAskedCount++;

		lastQuestionId = v.getId();

		return french ? v.getFrench() : v.getEnglish();
	}

	public static Vocab getQuestionVocab()
	{
		for (Vocab v : vocabList)
			if (v.getId() == lastQuestionId)
				return v;
		return null;

	}

	private boolean startsWithVowel(String word)
	{
		char first = word.charAt(0);
		for (char c : VOWELS)
			if (Character.toLowerCase(c) == Character.toLowerCase(first))
				return true;
		return false;
	}

	private static Vocab getRandom()
	{
		int item = rand.nextInt(vocabList.size());
		int i = 0;
		for (Vocab v : vocabList)
		{
			if (i == item)
				return v;
			i++;
		}

		return null;
	}


}
