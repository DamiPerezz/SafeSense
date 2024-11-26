package Model;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashear {
	public static String sha256(String parametro){
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(parametro.getBytes(StandardCharsets.UTF_8));
			
			StringBuilder hexadecimal = new StringBuilder();
			for(byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if(hex.length() == 1) hexadecimal.append('0');
				hexadecimal.append(hex);
			}
		return hexadecimal.toString();
		}catch(Exception e) {
			System.out.println("Error");
			return null;
		}
	}
	public static void main(String []args) {
		
		String input = "paquito";
		System.out.println(input);
		String salida = sha256(input);
		System.out.println(salida);
	}
}
