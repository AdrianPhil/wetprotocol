/**
  docs
  
  load from proteje save then remove the 

 */
public class Utils {
	public static  String firstLetterLowerCase(String string) {
		if(string==null) {
			return string;
		}
		if(string.length()==1) {
			return string.toLowerCase();
		}
		return string.substring(0, 0).toLowerCase()+string.substring(1, string.length());
	}
	

}
