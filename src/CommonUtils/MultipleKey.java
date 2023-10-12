package CommonUtils;

import java.util.ArrayList;

/*
 1. enum -> String : (toString)
 2. String -> enum : valueof
 3. enum -> short : (short)oridinal
 4. short -> enum : Enum.values()[shortValue]
 */

// Âü°í https://www.techiedelight.com/implement-map-with-multiple-keys-multikeymap-java/

public class MultipleKey {
	ArrayList<Object> multiKey = null;
	
	public MultipleKey(Object... objs) {
		multiKey = new ArrayList<Object>(objs.length);
	}
	
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || !multiKey.stream().anyMatch(myObj -> myObj == obj)) {
            return false;
        }
        
//        for(Object key : multiKey) {
//        	if(o == null || key.getClass())
//        }
//        
// 
//        if (key1 != null ? !key1.equals(key.key1) : key.key1 != null) {
//            return false;
//        }
// 
//        if (key2 != null ? !key2.equals(key.key2) : key.key2 != null) {
//            return false;
//        }
 
        return true;
    }
 
    @Override
    public int hashCode()
    {
        int result = 5;
        return result;
    }
 
    @Override
    public String toString() {
        return "";
    }
}
