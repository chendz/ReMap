package p.rn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import p.coffi.asm.Opcodes;
import p.rn.name.InitOut;

public class ClassInfo {

    public static class MemberInfo {
        public int access;
        public String desc;
        public String name;
        public Object value;
    }

    public int access;

    public Map<String, List<MemberInfo>> members = new HashMap<String, List<MemberInfo>>();

    public String name;

    public Set<String> parent = new HashSet<String>();
    
    //补充下superName和interfaces
    public String superName;
    public String[]  interfaces;
    


    public boolean equals(Object o) {
        return name.equals(((ClassInfo) o).name);
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return name;
    }
    
    public  boolean isInterface() {
        return (this.access & Opcodes.ACC_INTERFACE) != 0;
    }    	    
    
    //这里添加个输出的方法
    public String getSuffix(){
    	   if (this.superName != null){
    		   //读取extends部分
    		   String sp = this.superName;

    		  if ("java/lang/Enum".equals(sp)){
    			  return "Enum";
    		  }else
    		  if ("java/lang/Object".equals(sp)){
    			  String[]  arr = this.interfaces;
    			  //添加接口的处理
    			  if (arr !=null && arr.length>0){
    				  for(int i=0; i < arr.length; i++){
    					  String  str = arr[i];    					  
    					  if ("java/lang/Runnable".equals(str)){
    						  return "Runnable";
    					  }else
    					  if ("java/util/Comparator".equals(str)){
    						  return "Comparator";
    					  }else
    					  if ("java/io/DataInput".equals(str)){
    						  return "DataInput";
    					  }else
        				  if ("java/io/DataOutput".equals(str)){
        					  return "DataOutput";
        				 }    						 
    				  }
    			  }
    				
    			  
    			  return "";
    		  }else
    		  if ("java/lang/RuntimeException".equals(sp)){
    			  return "RuntimeException";
    		  }else

    		  if ("java/io/IOException".equals(sp)){
    			  return "IOException";
    		  }else 

    		  if ("java/lang/Thread".equals(sp)){
    			  return "Thread";
    		  }else
  
    		  if ("java/util/LinkedList".equals(sp)){
    			  return "LinkedList";
    		  }else
    		  if ("java/lang/Exception".equals(sp)){
    			  return "Exception";
    		  }else
    		  if ("java/util/EnumMap".equals(sp)){
    			  return "EnumMap";
    		  }else
    	
    		  if ("java/io/Writer".equals(sp)){
    			  return "Writer";
    		  }else
    	
    		  if ("java/io/BufferedOutputStream".equals(sp)){
    			  return "BufferedOutputStream";
    		  }else
    		 if ("org/eclipse/swt/events/SelectionAdapter".equals(sp)){
    			 return "SelectionAdapter";
    		 }else
    	     if ("org/eclipse/swt/widgets/Composite".equals(sp)){
    	    	 return "Composite";
    	     }
  
    		   
    		   
    	   }
    	  //不需要根据acc来判断了
//           if (isInterface())
//        	   return "_interface";
           return "";
    }
}
