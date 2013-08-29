package code
package snippet

trait XMLMatchers  {
  import scala.xml._
  import scala.xml.Utility.trim
  
  lazy val prettyPrinter = new PrettyXMLPrinter

  def like(result: NodeSeq, expected: NodeSeq): Option[String] = {
    val resultCanon = canonicalise(result)
    val expectedCanon = canonicalise(expected)
  
    if(resultCanon xml_== expectedCanon) None
    else Some(
         prettyPrinter.format(resultCanon)
       + " is not like expected "
       + prettyPrinter.format(expectedCanon)
       + ".\n" + resultCanon + "\n" + expectedCanon)
  }
  
  def canonicalise(n: NodeSeq): Node = try {
    _canonicalise(n)
  } catch {
    case e => {
      throw e
    }
  }
  
  def _canonicalise(nodeseq: NodeSeq): Node = {
    trim(
      XML.loadString(
        (<xml>{nodeseq}</xml>).toString
      )
    )
  }
  
  class PrettyXMLPrinter {
    def format(n: NodeSeq): String = {
      val sb = new StringBuilder
    
      n match {
        case node: Node => doNode(node, sb, 0)
        case _ => doNodeSeq(n, sb, 0)
      }
      
      sb.toString
    }
    
    def doNodeSeq(n: NodeSeq, sb: StringBuilder, i: Int) {
      doNode(n(0), sb, i)
    }
    
    def doChildren(e: Elem, sb: StringBuilder, i: Int) {
      for(n <- e.child) {
        doNode(n, sb, i+1)
      }
    }
    
    def doNode(n: Node, sb: StringBuilder, i: Int) {
      n match {
        case e: Elem => {
          sb.append('<').append(e.label)
          doAttrs(e, sb)
          sb.append('>')
          
          doChildren(e, sb, i)
          
          sb.append("</").append(e.label).append('>')
        }
        
        case Text(str) if !str.trim.isEmpty => sb.append(str.trim)
        case Text(_) => 
        case _ => sb.append(n.getClass + " " + n)
      }
    }
    
    def doAttrs(e: Elem, sb: StringBuilder) {
      for(a <- e.attributes.toList.sortWith(_.key < _.key)) {
        sb.append(' ')
          .append(a.key)
          .append("=\"")
          .append(a.value)
          .append("\"")
      }
    }
  }
}
