package p.rn.name;

import static p.rn.util.AccUtils.isPrivate;
import static p.rn.util.AccUtils.isPublic;
import static p.rn.util.AccUtils.isStatic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import p.rn.util.FileUtils;
import p.rn.ClassInfo;
import p.rn.ClassInfo.MemberInfo;
import p.rn.Scann;

public class InitOut {
    //存放两个全局引用
    public static HashSet<String> ggInterfaces = new HashSet<String>();
    public static HashSet<String> ggExtends= new HashSet<String>();
	
    private static Set<String> keywords = new HashSet<String>(Arrays.asList("abstract", "continue", "for", "new",
            "switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this",
            "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws",
            "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char",
            "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const",
            "float", "native", "super", "while"));

    private int clzIndex = 0;
    private Set<String> clzOutput = new TreeSet<String>();
    private Set<String> clzSet = new TreeSet<String>();
    private File from;

    private int maxLength = 40;
    private Set<String> memberOutput = new TreeSet<String>();

    private int minLength = 2;

//    private int pkgIndex = 0;

    private Set<String> pkgOutput = new TreeSet<String>();
    private Set<String> pkgSet = new TreeSet<String>();
    
//    private Map<String,Stack> clznameSet = new HashMap<String,Stack>();

    /**
     * 对Class进行命名
     * @param clz
     */
    private void doClass(String clz, ClassInfo info) {
        if (clzSet.contains(clz)) {
            return;
        }
        clzSet.add(clz);
        //有可能多个$符号
        int index = clz.lastIndexOf('$');
        if (index > 0) {      	        	
        	//前缀。。
            doClass(clz.substring(0, index), info);
            //后缀
            String cName = clz.substring(index + 1);
        	//如果不是数字,过滤处理                    
            try {
                Integer.parseInt(cName);
            } catch (Exception ex) {
                if (shouldRename(cName)) {
                    //clzMap.add(String.format("c %s=CI%03d%s", clz, clzIndex++, short4LongName(cName)));
                    String val = "c "+ clz +"="+cName +"_In"+info.getSuffix();
                    clzOutput.add(val);                	
                }
            }            
        } else {
            index = clz.lastIndexOf('/');
            
            if (index > 0) {
            	//包名
            	String  pkgName = clz.substring(0, index);
                doPkg(pkgName);
                

                //
                String inPkg = pkgShortName(pkgName);
                
                String cName = clz.substring(index + 1);
                if (shouldRename(cName)) {
                	//clzMap.add(String.format("c %s=C%03d%s", clz, clzIndex++, short4LongName(cName)));
                	


					if (pkgName.equals("com/pnfsoftware/jebglobal")) {
						if (cName.length() == 1) {
							char c1 = cName.charAt(0);
							if (c1 >= 'A' && c1 <= 'Z') {
								String val = "c " + clz + "=" + cName + info.getSuffix()+"_H";
								clzOutput.add(val);
							} else {
								String val = "c " + clz + "=" + cName +info.getSuffix()+ "_L";
								clzOutput.add(val);
							}
						} else if (cName.length() == 2) {
							char c1 = cName.charAt(0);
							if (c1 >= 'A' && c1 <= 'Z') {
								char c2 = cName.charAt(1);
								if (c2 >= 'A' && c2 <= 'Z') {
									String val = "c " + clz + "=" + cName
											+info.getSuffix()+ "_HH";
									clzOutput.add(val);
								} else {
									String val = "c " + clz + "=" + cName
											+info.getSuffix()+ "_HL";
									clzOutput.add(val);
								}
							} else {
								char c2 = cName.charAt(1);
								if (c2 >= 'A' && c2 <= 'Z') {
									String val = "c " + clz + "=" + cName
											+ info.getSuffix()+"_LH";
									clzOutput.add(val);
								} else {
									String val = "c " + clz + "=" + cName
											+ info.getSuffix()+"_LL";
									clzOutput.add(val);
								}
							}
						}
					} else {
						String val = "c " + clz + "=" + cName + inPkg
								+ info.getSuffix();
						clzOutput.add(val);
					}
                    
                    

                }
            } else {
            	//这个没有包
                if (shouldRename(clz)) {
                    //clzMap.add(String.format("c %s=CI_%03d%s", clz, clzIndex++, short4LongName(clz)));
                	String val = "c "+clz+"="+clz+"_inner_"+ (clzIndex++);
                	clzOutput.add(val);
                }
            }
        }
    }

	private String pkgShortName(String pkgName) {

		if ("com/pnfsoftware/jeb/corei".equals(pkgName)){
		      return  "_corei";
		}
		if ("com/pnfsoftware/jeb/corei/parsers/apk".equals(pkgName)){
		      return  "_apk";
		}
		if ("com/pnfsoftware/jeb/corei/parsers/dex".equals(pkgName)){
		      return  "_dex";
		}
		if ("com/pnfsoftware/jeb/corei/parsers/dexdec".equals(pkgName)){
		      return  "_dexdec";
		}

		if ("com/pnfsoftware/jeb/corei/parsers/macho".equals(pkgName)){
			return "_macho";
		}
		
		
		if ("com/pnfsoftware/jeb/corei/parsers/odex".equals(pkgName)){
			return "_odex";
		}		
		
		if ("com/pnfsoftware/jeb/corei/parsers/winpe".equals(pkgName)){
			return "_winpe";
		}		
		
		if ("com/pnfsoftware/jeb/corei/parsers/xml".equals(pkgName)){
			return "_xml";
		}			
		
		if ("com/pnfsoftware/jeb/corei/parsers/zip".equals(pkgName)){
			return "_zip";
		}
		
		if ("com/pnfsoftware/jeb/corei/debuggers/dexdbg".equals(pkgName)){
			return "_dexdbg";
		}
		
		if ("com/pnfsoftware/jeb/corei/parsers".equals(pkgName)){
			return "_parser";
		}			
		
		if ("com/pnfsoftware/jeb/corei/parsers/utf8".equals(pkgName)){
			return "_utf";
		}					
		
		if ("com/pnfsoftware/jeb".equals(pkgName)){
		      return  "_jeb";
		}		
			
		return "";
	}

    private String short4LongName(String name) {
        if (name.length() > maxLength) {
            return "x" + Integer.toHexString(name.hashCode());
        } else {
            return name;
        }
    }
    
	/** Descriptor code string. */
	static final String DESC_BYTE = "B";
	/** Descriptor code string. */
	static final String DESC_CHAR = "C";
	/** Descriptor code string. */
	static final String DESC_DOUBLE = "D";
	/** Descriptor code string. */
	static final String DESC_FLOAT = "F";
	/** Descriptor code string. */
	static final String DESC_INT = "I";
	/** Descriptor code string. */
	static final String DESC_LONG = "J";
	/** Descriptor code string. */
	static final String DESC_OBJECT = "L";
	/** Descriptor code string. */
	static final String DESC_SHORT = "S";
	/** Descriptor code string. */
	static final String DESC_BOOLEAN = "Z";
	/** Descriptor code string. */
	static final String DESC_VOID = "V";
	/** Descriptor code string. */
	static final String DESC_ARRAY = "[";    
	static String parseDesc(String desc, String sep) {
		String params = "", param;
		char c;
		int i, len, arraylevel = 0;
		boolean didone = false;

		len = desc.length();
		for (i = 0; i < len; i++) {
			c = desc.charAt(i);
			if (c == DESC_BYTE.charAt(0)) {
				param = "byte";
			} else if (c == DESC_CHAR.charAt(0)) {
				param = "char";
			} else if (c == DESC_DOUBLE.charAt(0)) {
				param = "double";
			} else if (c == DESC_FLOAT.charAt(0)) {
				param = "float";
			} else if (c == DESC_INT.charAt(0)) {
				param = "int";
			} else if (c == DESC_LONG.charAt(0)) {
				param = "long";
			} else if (c == DESC_SHORT.charAt(0)) {
				param = "short";
			} else if (c == DESC_BOOLEAN.charAt(0)) {
				param = "boolean";
			} else if (c == DESC_VOID.charAt(0)) {
				param = "void";
			} else if (c == DESC_ARRAY.charAt(0)) {
				arraylevel++;
				continue;
			} else if (c == DESC_OBJECT.charAt(0)) {
				int j;
				j = desc.indexOf(';', i + 1);
				if (j < 0) {
					System.out
							.println("Warning: Parse error -- can't find a ; in "
									+ desc.substring(i + 1));
					param = "<error>";
				} else {
					if (j - i > 10
							&& desc.substring(i + 1, i + 11).compareTo(
									"java/lang/") == 0)
						i = i + 10;
					param = desc.substring(i + 1, j);
					// replace '/'s with '.'s
					param = param.replace('/', '.');
					//截取最后部分
					int pos = param.lastIndexOf('.');
					param = pos>=0? param.substring(pos+1):param;
					
					i = j;
				}
			} else {
				param = "???";
			}
			if (didone)
				params = params + sep;
			params = params + param;
			while (arraylevel > 0) {
				params = params + "array";
				arraylevel--;
			}
			didone = true;
		}
		return params;
	}    

    private void doMethod(String owner, MemberInfo member, int x) {
        if (x > 0 || shouldRename(member.name)) {
        	//这个是方法---因为有(，表面是方法
            if (member.desc.indexOf('(') >= 0) {
                StringBuilder sb = new StringBuilder();
//                sb.append(isStatic(member.access) ? "M" : "m");
//                if (isPrivate(member.access)) {
//                    sb.append("p");
//                } else if (isPublic(member.access)) {
//                    sb.append("P");
//                }
                if (x > 0) {
                    sb.append(x);
                }
                sb.append(short4LongName(member.name));
                //添加修饰符
                sb.append(isStatic(member.access)?"_static":"");
                sb.append(isPrivate(member.access)?"_pri":"");          
                
                String d = member.desc;
        		
        		int i = d.indexOf('(');
        		if (i >= 0) {
        			int j = d.indexOf(')', i + 1);
        			if (j >= 0) {
        				//String val = parseDesc(d.substring(i + 1, j), ",");
        				//读取()Xxxxx的Xxxxx部分
        				String result = d.substring(j+1);
        				String dd = parseDesc(result,",");
//        				System.out.println("Method>>"+dd);
        				sb.append("_"+dd);
        			}
        		}
       
                
                if (x > 0) {
                    memberOutput.add("m " + owner + "." + member.name + member.desc + "=" + sb.toString());
                } else {
                    memberOutput.add("m " + owner + "." + member.name
                            + member.desc.substring(0, member.desc.indexOf(')') + 1) + "=" + sb.toString());
                }
            } else {
                StringBuilder sb = new StringBuilder();
//                sb.append(isStatic(member.access) ? "F" : "f");
//                if (isPrivate(member.access)) {
//                    sb.append("p");
//                } else if (isPublic(member.access)) {
//                    sb.append("P");
//                }
                if (x > 0) {
                    sb.append(x);
                }
                sb.append(short4LongName(member.name));
                
                String d = member.desc;
                
   
				String dd = parseDesc(d, ",");
//				System.out.println("Field>>" + dd);
        		sb.append("_"+dd);
                
                sb.append(isPrivate(member.access)?"_pri":"");
                sb.append(isStatic(member.access) ? "_static_fld" : "_fld");
                
//                System.out.println(member.desc+ ">>>"+sb.toString());                
                
                if (x > 0) {
                    memberOutput.add("m " + owner + "." + member.name + "[" + member.desc + "]" + "=" + sb.toString());
                } else {
                    memberOutput.add("m " + owner + "." + member.name + "=" + sb.toString());
                }
            }
        }
    }
    /**
     * 执行输出
     * @throws IOException
     */
    private void doOut() throws IOException {
    	//遍历jar包
        Map<String, ClassInfo> map = Scann.scanLib(from);
        
        //这里应该过滤一份比较好的名称
 
        for (ClassInfo info : map.values()){
//        	System.out.println("::::"+info.name);
        	String val = info.name;
        	int idx = val.lastIndexOf('/');
        	String clzz =  idx!=-1? val.substring(idx+1):val;
        	if (clzz!=null && clzz.length()>7){ //8个字符的，我们认为是一个有效的接口名称
        	    if (info.isInterface()){
        	    	ggInterfaces.add(val);//存放完整的名称
        	    }else{
        	    	ggExtends.add(val);//存放完整的名称
        	    }
        	}
        }
        
        //遍历infoMap
        for (ClassInfo info : map.values()) {
//        	if (!info.name.equals("jeb/DP/th/th/jI")) continue;
//        	if (!info.name.equals("android/support/v4/view/s")) continue;
        	//逐个处理
//        	System.out.println("begin init.."+info.name);
        	//做些过滤出来吧。
        	String fullName = info.name;
        	int  idx = fullName.lastIndexOf('/');
        	String clz = idx !=-1? fullName.substring(idx+1, fullName.length()):fullName;
           //这里来拦截R特殊类吧
           	if ( "R$anim".equals(clz) || 
           			"R$array".equals(clz) || 
           			"R$attr".equals(clz) || 
           			"R$bool".equals(clz) || 
           			"R$color".equals(clz) || 
           			"R$dimen".equals(clz) || 
           			"R$drawable".equals(clz) || 
           			"R$id".equals(clz) || 
           			"R$integer".equals(clz) || 
           			"R$layout".equals(clz) || 
           			"R$menu".equals(clz) || 
           			"R$plurals".equals(clz) || 
           			"R$string".equals(clz) || 
           			"R$style".equals(clz) || 
           			"R$styleable".equals(clz) || 
           			"R$xml".equals(clz) || 
           			"R".equals(clz) // 这个是否可行？
           			){
           		System.out.println("R is special class."+ clz);
           		continue;
           	}              	
        	
            doClass(info.name, info);
            for (List<MemberInfo> ms : info.members.values()) {
                if (ms.size() == 1) {
                    for (MemberInfo m : ms) {
                        doMethod(info.name, m, 0);
                    }
                } else {
                    int i = 1;
                    for (MemberInfo m : ms) {
                        doMethod(info.name, m, i++);
                    }
                }
            }
        }
    }

    /**
     * 对pkg进行命名
     * @param pkg
     */
    private void doPkg(String pkg) {
        if (pkgSet.contains(pkg)) {
            return;
        }
        pkgSet.add(pkg);
        int index = pkg.lastIndexOf('/');
        if (index > 0) {
        	//前缀
            doPkg(pkg.substring(0, index));
            //后缀
            String cName = pkg.substring(index + 1);
            if (shouldRename(cName)) {
//                pkgMap.add(String.format("p %s=p%02d%s", pkg, pkgIndex++, short4LongName(cName)));
                String val = "p "+pkg+"="+ cName +"";//不用添加_pkg来区分
                pkgOutput.add(val);            	
            }
        } else {
            if (shouldRename(pkg)) {
//                pkgMap.add(String.format("p %s=p%02d%s", pkg, pkgIndex++, short4LongName(pkg)));
                String val = "p "+pkg+"="+ pkg +"";//_pkg
                pkgOutput.add(val);
            }
        }
    }

    public InitOut from(File from) {
        this.from = from;
        return this;
    }

    public InitOut maxLength(int m) {
        this.maxLength = m;
        return this;
    }

    public InitOut minLength(int m) {
        this.minLength = m;
        return this;
    }

    private boolean shouldRename(String s) {
        return s.length() > maxLength || s.length() < minLength || keywords.contains(s);
    }

    public void to(File config) throws IOException {
        doOut();
        List<String> list = new ArrayList<String>();
        list.addAll(pkgOutput);
        list.addAll(clzOutput);
        list.addAll(memberOutput);
        FileUtils.writeLines(config, "UTF-8", list);
    }

}
