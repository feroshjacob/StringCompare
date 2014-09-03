package cb.index

object StringCompare {
  val threshold = 0.8f                            //> threshold  : Float = 0.8
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
    def getCurrentCluster(existingList: List[Int], searchResult: List[(Int, String, Float)]) = {
    require(!searchResult.isEmpty, "Search result should not empty, atleast the document itself should be returned")
    val init: (List[Int], List[Int], Float) = (existingList, List(), searchResult.head._3)
    searchResult.foldLeft(init)((p, q) => {
    
      if(existingList.contains(q._1)) (p._1, p._2, q._3)
      else if (q._3 >= threshold * p._3) (q._1 :: p._1, q._1 :: p._2, q._3)
      else p

    })
  }                                               //> getCurrentCluster: (existingList: List[Int], searchResult: List[(Int, String
                                                  //| , Float)])(List[Int], List[Int], Float)
            
     val list =  List( (1, "Hello", 2.3f),(2, "Hello", 2.0f), (3, "Hello", 1.2f) )
                                                  //> list  : List[(Int, String, Float)] = List((1,Hello,2.3), (2,Hello,2.0), (3,H
                                                  //| ello,1.2))
                                                  
        getCurrentCluster(List(2), list)          //> res0: (List[Int], List[Int], Float) = (List(1, 2),List(1),2.0)
}