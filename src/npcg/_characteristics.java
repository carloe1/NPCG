package npcg;

import java.util.Map;

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
	
	static void GenerateCharacteristics(Map<String, PdfFormField> fields) {
		
		String[] c3d6 = {"STR", "CON", "DEX", "APP", "POW"};
		String[] c2d6 = {"SIZ", "INT", "EDU"};
		
		for (String characteristic : c3d6) {
			roll3D6(fields, characteristic);
		}
		
		for (String characteristic : c2d6) {
			roll3D6(fields, characteristic);
		}
		
		
		
	}
	
}
