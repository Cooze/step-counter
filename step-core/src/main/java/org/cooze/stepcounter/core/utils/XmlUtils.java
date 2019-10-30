package org.cooze.stepcounter.core.utils;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-07-29
 **/
public final class XmlUtils {


    public static Set<String> readKeyFields(String file) {
        return readData(file, "/schema/body/keyFields/keyField");
    }

    public static Set<String> readCountFields(String file) {
        return readData(file, "/schema/body/countFields/countField");
    }

    public static String readSchemaName(String file) {
        return readData(file, "/schema/head/name").toArray(new String[]{})[0];
    }

    public static String readSchemaTimeFrame(String file) {
        return readData(file, "/schema/head/timeFrame").toArray(new String[]{})[0];
    }

    public static Set<String> readKeyFields(InputStream inputStream) {
        return readData(inputStream, "/schema/body/keyFields/keyField");
    }

    public static Set<String> readCountFields(InputStream inputStream) {
        return readData(inputStream, "/schema/body/countFields/countField");
    }

    public static String readSchemaName(InputStream inputStream) {
        return readData(inputStream, "/schema/head/name").toArray(new String[]{})[0];
    }

    public static String readSchemaTimeFrame(InputStream inputStream) {
        return readData(inputStream, "/schema/head/timeFrame").toArray(new String[]{})[0];
    }

    private static Set<String> readData(InputStream inputStream, String xpath) {
        Set<String> paths = new HashSet<>();
        try {
            VTDGen vg = new VTDGen();

            if (inputStream != null) {
                if (!parseFile(vg, readBytes(inputStream), false)) {
                    return paths;
                }
            }
            AutoPilot autoPilot = new AutoPilot();
            VTDNav vn = vg.getNav();
            autoPilot.bind(vn);
            autoPilot.selectXPath(xpath);
            int result;
            while ((result = autoPilot.evalXPath()) != -1) {
                String content = vn.toNormalizedString(vn.getText());
                if (!StringUtils.isEmpty(content)) {
                    paths.add(content.trim());
                }
            }
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
            return paths;
        }
    }

    private static Set<String> readData(String file, String xpath) {
        Set<String> paths = new HashSet<>();
        try {
            VTDGen vg = new VTDGen();

            if (new File(file).exists()) {
                if (!vg.parseFile(file, false)) {
                    return paths;
                }
            } else {
                //打包后在jar包中读取
                InputStream inputStream = XmlUtils.class.getClassLoader().getResourceAsStream("/Authentication.xml");
                if (inputStream != null) {
                    if (!parseFile(vg, readBytes(inputStream), false)) {
                        return paths;
                    }
                }
            }
            AutoPilot autoPilot = new AutoPilot();
            VTDNav vn = vg.getNav();
            autoPilot.bind(vn);
            autoPilot.selectXPath(xpath);
            int result;
            while ((result = autoPilot.evalXPath()) != -1) {
                String content = vn.toNormalizedString(vn.getText());
                if (!StringUtils.isEmpty(content)) {
                    paths.add(content.trim());
                }
            }
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
            return paths;
        }
    }


    private static boolean parseFile(VTDGen vg, byte[] doc, boolean b) {
        try {
            vg.setDoc(doc);
            vg.parse(b);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    private static byte[] readBytes(InputStream inputStream) {
        try {
            int i = inputStream.available();
            byte[] buf = new byte[inputStream.available()];
            int len = -1;
            while ((len = inputStream.read(buf, 0, i)) != -1) ;
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
