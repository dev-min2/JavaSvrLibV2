package CoreAcitive;

import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/*
IoC/DI�� ���� ��ϵ� XML������ �Ľ��Ͽ� ���.
*/
public class ApplicationBeanLoader {
	@SuppressWarnings("null")
	public HashMap<AbstractMap.SimpleEntry<String, Class>,Object> parseBean(String xmlPath) throws Exception {
		if(xmlPath == null || xmlPath.isEmpty())
			return null;
		
		var ret = new HashMap<AbstractMap.SimpleEntry<String, Class>,Object>();
		// Class �Ľ̿����� ��� �̿��Ѵ�.
		HashMap<String,Class> temp = new HashMap<String,Class>();
		
		String accessXmlPath = "resource/beanText.xml";
		
		Path currentRelativePath = Paths.get("");
		String currentAbsoulutePath = currentRelativePath.toAbsolutePath().toString();
		
		String[] splitArray = currentAbsoulutePath.split("\\\\");
		if(splitArray[splitArray.length - 1].equals("bin")) {
			accessXmlPath = "../resource/beanText.xml";
		}
		
		Document xml = null;
		InputSource is = new InputSource(new FileReader(accessXmlPath));
		xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		xml.getDocumentElement().normalize();

		NodeList childNodelist = xml.getDocumentElement().getElementsByTagName("bean");		
		if(childNodelist.getLength() > 0) {
			for(int nodeIndex = 0; nodeIndex < childNodelist.getLength(); ++nodeIndex) {
				Node xmlNode = childNodelist.item(nodeIndex);
				
				if(xmlNode.getNodeName().equals("bean")) {
					NamedNodeMap nodeMap = xmlNode.getAttributes();
					
					String id = nodeMap.getNamedItem("id").getTextContent();
					String className = nodeMap.getNamedItem("class").getTextContent();
					Object obj = null;
					
					if(id == null || id.isEmpty() || className == null || className.isEmpty())
						throw new Exception("bean xml error(id == null || id.isEmpty() || className == null || className.isEmpty())");
					
					// �ش��ϴ� Ŭ������ ���ٸ� ���⼭ Exception �߻�.
					Class match = Class.forName(className);
					
					// �� ��ü�� ���� ���
					NodeList beanChildNodeList = xmlNode.getChildNodes();
					
					// ������ ����(DI -> ����� ������ ���Ը�)
					if(beanChildNodeList.getLength() > 0) {
						Class[] refClassArray = new Class[match.getDeclaredConstructors()[0].getParameterCount()];
						String[] refIDs = new String[refClassArray.length];
						int idx = 0;
						for(int beanChildNodeIndex = 0; beanChildNodeIndex < beanChildNodeList.getLength(); ++beanChildNodeIndex) {
							Node beanChildNode = beanChildNodeList.item(beanChildNodeIndex);
							if(beanChildNode.getNodeName().equals("constructor-arg")) {
								NamedNodeMap beanNodeMap = beanChildNode.getAttributes();
								
								String refId = beanNodeMap.getNamedItem("ref").getTextContent();
								if(!temp.containsKey(refId))
									throw new Exception("not containsKey refID");
								
								refClassArray[idx] = temp.get(refId);
								refIDs[idx++] = refId;
							}
						}
						Constructor constructor = match.getConstructor(refClassArray);
						Object[] argumentObject = new Object[refClassArray.length];
						for(int i = 0; i < argumentObject.length; ++i) 
							argumentObject[i] = ret.get(new AbstractMap.SimpleEntry<String, Class>(refIDs[i],refClassArray[i])); 
						
						obj = constructor.newInstance(argumentObject);
					}
					else {
						// �⺻ ������(����x)
						obj = match.getDeclaredConstructor().newInstance();
					}
					
					temp.put(id, match);
					ret.put(new AbstractMap.SimpleEntry<String, Class>(id,match), obj);
				}
			}
		}
		
		return ret;
	}
}

