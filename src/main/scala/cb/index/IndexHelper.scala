package cb.index

import java.io.StringReader
import scala.io.Source
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.store.FSDirectory
import java.io.File

object IndexHelper {

  val indexPath = "cbIndex"
  val version = Version.LUCENE_4_9

  def createIndex(list: List[(Int, String)], dir: Directory) = {
    def indexRecipes(writer: IndexWriter, f: (Int, String)) = {
      val doc = new Document();
      doc.add(new TextField("content", new StringReader(f._2)));
      doc.add(new StringField("documentID", f._1 + "", Field.Store.YES));

      writer.addDocument(doc);
    }
    require(!list.isEmpty, "Something wrong, indexing an empty list")
    //  val dir = new RAMDirectory()//FSDirectory.open(new File(indexPath))
    val analyzer = new StandardAnalyzer(version)
    val iwc = new IndexWriterConfig(version, analyzer);
    val writer = new IndexWriter(dir, iwc);

    list.foreach(f => {
      indexRecipes(writer, f);
      //    println("added")
    })
    // println("closed")
    writer.close();
  }

  private def getScoreDocs(text: String, reader: Directory) = {
    require(text.trim().length() > 0, "search text is empty")
    val searcher = new IndexSearcher(DirectoryReader.open(reader));

    val analyzer = new StandardAnalyzer(version)
    val parser = new QueryParser(version, "content", analyzer);
    val query = parser.parse(text);
    val results = searcher.search(query, 200);
    results.scoreDocs
  }
  def searchText(text: String, reader: Directory): List[(Int, String, Float)] = {
    val searcher = new IndexSearcher(DirectoryReader.open(reader));

    for (i <- getScoreDocs(text, reader).toList.map(f => (searcher.doc(f.doc).get("documentID").toInt, searcher.doc(f.doc).get("content"), f.score))) yield i

  }

  val threshold = 0.95f
    val NonSpaceSymbols = """[\~\`\!\@\#\$\%\^\&\*\(\)\-\_\+\=\{\}\[\]\|\\\/\:\;\"\'\<\>\,\.\?]"""
  def getCurrentCluster(searchResult: List[(Int, String, Float)]) = {
    require(!searchResult.isEmpty, "Search result should not empty, atleast the document itself should be returned")
    val init: (List[Int], Float) = (List(),  searchResult.head._3)
    searchResult.foldLeft(init)((p, q) => {

     
       if (q._3 >= threshold * p._2) (q._1 :: p._1, q._3)
      else p

    })
  } //> getCurrentCluster: (existingList: List[Int], searchResult: List[(Int, String

  def main(args: Array[String]) {
    //		val list = List((1,"This is a boy"),(2, "This is a boy and girl") , (3,"This is so cute"))

    var lineN = 0
    val list = Source.fromFile("dataportaldata.txt").getLines.map(f => {
      lineN += 1
      (lineN, f)
    }).toList

    val directory = new RAMDirectory()
    createIndex(list, directory)
    val reader = DirectoryReader.open(directory)
    println("Total documents:" + reader.document(0).get("content"))
  
    list foreach(f => {
   
         val modified = f._2.replaceAll(NonSpaceSymbols, " ").trim
       val searchResult =searchText(modified, directory)
        val searchCount = getCurrentCluster(searchResult )
      
        println(f._1 +"\t"+ searchCount._1.size + "\t" +  searchCount._1.slice(0,5).mkString(","))
       //  println(searchCount._1.mkString("\n"))
     // }
    })
  }

}
