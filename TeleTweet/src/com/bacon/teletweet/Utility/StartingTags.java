package com.bacon.teletweet.Utility;

import java.util.ArrayList;
import java.util.List;

public class StartingTags
{
	public static List<String> getStartingTags(String showname)
	{
		ArrayList<String> tags = new ArrayList<String>(10);
		if(showname.equals("Breaking Bad"))
		{
			tags.add("#BreakingBad");
			tags.add("#WalterWhite");
			tags.add("#BryanCranston");
		}
		else if(showname.equals("Family Guy"))
		{
			tags.add("#FamilyGuy");
			tags.add("#SethMcfarlane");
			tags.add("#Meg");
			tags.add("#MilaKunis");
		}
		else if(showname.equals("Dexter"))
		{
			tags.add("#Dexter");
			tags.add("#deb");
			tags.add("#masuka");
			tags.add("#sliceoflife");
			tags.add("#DrVogel");
		}
		else if(showname.equals("The Bridge"))
		{
			tags.add("#TheBridge");
		}
		else if(showname.equals("The Simpsons"))
		{
			tags.add("#Simpsons");
			tags.add("#bart");
			tags.add("#homer");
			tags.add("#couchgag");
		}
		else if(showname.equals("Homeland"))
		{
			tags.add("#Homeland");
		}
		else if(showname.equals("Under the Dome"))
		{
			tags.add("#UnderTheDome");
			tags.add("#FourthHand");
			tags.add("#AliceCalvert");
		}
		else if(showname.equals("Hannibal"))
		{
			tags.add("#Hannibal");
			tags.add("#HannibalLector");
			tags.add("#WillGraham");
			tags.add("#Abigail");
		}
		else if(showname.equals("Mad Men"))
		{
			tags.add("#MadMen");
			tags.add("#DonDraper");
			tags.add("#Peggy");
			tags.add("#JonHamm");
			tags.add("#ChristinaHendricks");
		}
		else if(showname.equals("Walking Dead"))
		{
			tags.add("#WalkingDead");
		}
		
		return tags;
	}
}
