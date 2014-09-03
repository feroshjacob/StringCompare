package cb.index

object StringCompare {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(63); 
  val threshold = 0.8f;System.out.println("""threshold  : Float = """ + $show(threshold ));$skip(44); 
  println("Welcome to the Scala worksheet");$skip(512); 
    def getCurrentCluster(existingList: List[Int], searchResult: List[(Int, String, Float)]) = {
    require(!searchResult.isEmpty, "Search result should not empty, atleast the document itself should be returned")
    val init: (List[Int], List[Int], Float) = (existingList, List(), searchResult.head._3)
    searchResult.foldLeft(init)((p, q) => {
    
      if(existingList.contains(q._1)) (p._1, p._2, q._3)
      else if (q._3 >= threshold * p._3) (q._1 :: p._1, q._1 :: p._2, q._3)
      else p

    })
  };System.out.println("""getCurrentCluster: (existingList: List[Int], searchResult: List[(Int, String, Float)])(List[Int], List[Int], Float)""");$skip(96); 
            
     val list =  List( (1, "Hello", 2.3f),(2, "Hello", 2.0f), (3, "Hello", 1.2f) );System.out.println("""list  : List[(Int, String, Float)] = """ + $show(list ));$skip(92); val res$0 = 
                                                  
        getCurrentCluster(List(2), list);System.out.println("""res0: (List[Int], List[Int], Float) = """ + $show(res$0))}
}
