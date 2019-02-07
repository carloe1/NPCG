package npcg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import com.itextpdf.forms.fields.PdfFormField;

public class _characteristics {
	
	static void roll3D6(Map<String, PdfFormField> fields, String characteristic) {
		_die die = new _die();
		int value = die.nroll(3, 6) * 5;
		fields.get(characteristic).setValue(String.valueOf(value));
		fields.get(characteristic + "_half").setValue(String.valueOf((int)value/2));
		fields.get(characteristic + "_fifth").setValue(String.valueOf((int)value/5));
		
	}
	
	static void roll2D6(Map<String, PdfFormField> fields, String characteristic) {
		_die die = new _die();
		int value = (die.nroll(2, 6) + 6) * 5;
		fields.get(characteristic).setValue(String.valueOf(value));
		fields.get(characteristic + "_half").setValue(String.valueOf((int)value/2));
		fields.get(characteristic + "_fifth").setValue(String.valueOf((int)value/5));
	}
	
	static int findMaxValue(int[] values) {
		int max = -1;
		for (int value : values) {
			if (value > max) {
				max = value;
			}
		}
		
		return max;
	}
	
	static boolean contains(int[] array, int value) {
		for (int i: array) {
			if (value == i) {
				return true;
			}
		}
		
		return false;
	}
	
	static int[] getSkillIndexes(int total_skill, int num_skills) {
		int[] indexes = new int[num_skills];
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i=0; i < total_skill; i++) {
			list.add(i);
		}
		
		Collections.shuffle(list);
		for (int i=0; i < num_skills; i++) {
			indexes[i] = list.get(i);
		}
		
		return indexes;
	}
	
	static double[] divideUniformlyRandomly(double number, int part) {
	    double uniformRandoms[] = new double[part];
	    Random random = new Random();

	    double mean = number / part;
	    double sum = 0.0;

	    for (int i=0; i<part / 2; i++) {
	        uniformRandoms[i] = random.nextDouble() * mean;

	        uniformRandoms[part - i - 1] = mean + random.nextDouble() * mean;

	        sum += uniformRandoms[i] + uniformRandoms[part - i -1];
	    }
	    uniformRandoms[(int)Math.ceil(part/2)] = uniformRandoms[(int)Math.ceil(part/2)] + number - sum;

	    return uniformRandoms;
	}
	

	static void GenerateCharacteristics(Map<String, PdfFormField> fields, String resources_path) {
		
		String[] c3d6 = {"STR", "CON", "DEX", "APP", "POW"};
		String[] c2d6 = {"SIZ", "INT", "EDU"};
		
		for (String characteristic : c3d6) {
			roll3D6(fields, characteristic);
		}
		
		for (String characteristic : c2d6) {
			roll3D6(fields, characteristic);
		}
		
		// Values ======================================================================================
		int str = Integer.parseInt(fields.get("STR").getValueAsString());
		int siz = Integer.parseInt(fields.get("SIZ").getValueAsString());
		int con = Integer.parseInt(fields.get("CON").getValueAsString());
		int dex = Integer.parseInt(fields.get("DEX").getValueAsString());
		int pow = Integer.parseInt(fields.get("POW").getValueAsString());
		int app = Integer.parseInt(fields.get("APP").getValueAsString());
		int inte = Integer.parseInt(fields.get("INT").getValueAsString());
		int edu = Integer.parseInt(fields.get("EDU").getValueAsString());
		int[] values = {str, siz, con, dex, pow, app, inte, edu};
		int maxValue = findMaxValue(values);
		
		// CALCULATE SANITY =============================================================================
		int Sanity_Start = pow;
		fields.get("Sanity_Start").setValue(String.valueOf(Sanity_Start));
		fields.get("CurrentSanity").setValue(String.valueOf(Sanity_Start));

		
		// CALCULATE MAGIC POINTS =======================================================================
		int CurrentMP = pow/5;
		fields.get("CurrentMP").setValue(String.valueOf(CurrentMP));

		// CALCULATE LUCK TOTAL =========================================================================
		_die die = new _die();
		int luck = die.nroll(3, 6) * 5;
		fields.get("LuckTotal").setValue(String.valueOf(luck));
		
		// Calculate Damage Bonus =========================================================================
		int strsiz = str + siz;
		String build;
		String attackBonus;
		
		if (strsiz <= 64) {build = "-2";attackBonus = "-2";}
		else if (strsiz>=65 && strsiz<=84) {build = "-1";attackBonus = "-1";}
		else if (strsiz>=85 && strsiz<=124 ) {build = "0";attackBonus = "None";}
		else if (strsiz>=125 && strsiz<=164) {build = "1";attackBonus = "+1D4";}
		else if (strsiz>=165 && strsiz<=204) {build = "2";attackBonus = "+1D6";}
		else if (strsiz>=205 && strsiz<=284) {build = "3";attackBonus = "+2D6";}
		else if (strsiz>=285 && strsiz<=364) {build = "4";attackBonus = "+3D6";}
		else if (strsiz>=365 && strsiz<=444) {build = "5";attackBonus = "+4D6";}
		else {build = "6";attackBonus = "+5D6";}
		
		fields.get("Build").setValue(build);
		fields.get("DamageBonus").setValue(attackBonus);
		
		// Calculate HIT POINT ============================================================================
		int hitpoints = (con + siz) / 10;
		fields.get("CurrentHP").setValue(String.valueOf(hitpoints));
		
		// Calculate Movement Rate
		int mov;
		if (dex < siz && str < siz) { mov = 7;}
		else if (dex > siz && str > siz) { mov = 9;}
		else { mov = 8;}
		
		fields.get("MOV").setValue("" + mov);
		
		// SKILLS ==========================================================================================
		int skill_points = (maxValue * 4) + (inte * 2);
		int total_skills = die.nroll(1, 5) + 10;
		double[] skills = divideUniformlyRandomly(skill_points, total_skills);
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		
		try {
			Scanner scan = new Scanner(new File(resources_path + "/skills"));
			String[] line;
			while(scan.hasNextLine()) {
				line = scan.nextLine().split("\\s+");
				map.put(line[0], Integer.parseInt(line[1]));
			}
			
			int cur_index = 0;
			int skill_index = 0;
			int skill_num = map.size();
			int[] skill_indexes = _characteristics.getSkillIndexes(skill_num, total_skills);
			Arrays.sort(skill_indexes);
			
			//System.out.println("Total Skills Points: " + skill_points);
			//System.out.println("Total Skills: " + total_skills);
			//for (int i=0; i < total_skills; i++) {
			//	System.out.println(String.format("%f / %d", skills[i], skill_indexes[i]));
			//}
			
			
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				String skill = entry.getKey(); 
				int value = entry.getValue();
				if (skill_index < total_skills && skill_indexes[skill_index] == cur_index) {
					value += (int) skills[skill_index];
					skill_index++;
				}
				
				//System.out.println(skill + " / "  + value);
				fields.get(skill).setValue("" + value);
				cur_index++;
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
