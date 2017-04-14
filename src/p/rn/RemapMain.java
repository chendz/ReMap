package p.rn;

import java.io.File;

import p.rn.util.FilenameUtils;
import p.rn.name.InitOut;
import p.rn.name.Renamer;

public class RemapMain {

	public static void main(String[] args) {
		String BASE = System.getProperty("user.dir");

		if (args.length < 1){
			System.out.println("Usage: java -jar ReMap.jar  xxxx.jar");
			System.out.println("xxxx.jar in current dir.");
			System.exit(0);
		}
		String infile = BASE+File.separator + args[0];

		int min = 3;
		int max = 40;
		// �����ļ�
		File input = new File(infile);

		File config = new File(BASE, FilenameUtils.getBaseName(input.getName())
				+ "-deobf-init.ini");

		File output = new File(BASE, FilenameUtils.getBaseName(input.getName())
				+ "-remap.jar");

		try {
			System.out.println("generate " + input + " -> " + config);
			new InitOut().from(input).maxLength(max).minLength(min).to(config);

			System.out.println("remap " + input + " -> " + output);
			new Renamer().from(input).withConfig(config).to(output);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
