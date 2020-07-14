package nuc.edu.cn;

import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;


import javax.print.Doc;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName luceneSecond
 * @Author liuxiaobo
 * @Date 2020/6/21&18:55
 * @Version 1.0
 **/
public class luceneManager{

    /*
        IndexWriterConfig默认加载的是StandardAnalyzer（标准解析器）
        IKAnalyzer是自定义加载器  需要有IKAnalyzer的jar包以及其配置文件
        IKAnalyzer的文件介绍  hotWork是需要作为词汇查询出来的词汇存储文件
                             stopWork是需要屏蔽某些词汇词汇存储文件
        使用时需要将IKAnalyzer对象作为参数加入IndexWriterConfig的构造方法中
        StringField是不可以分析、可以索引、可以选择是否存储的域
        LongPoint是可以分析、可以索引、默认不存储的域  可以做运算但是不能够将值取出来  一般情况与StoreField一起使用  IntPoint、FloatPoint同样如此
        StoreField是不可以分析、索引、默认存储的域
        TextField是可以分析、索引、可以选择是否存储的域

    */

    private static IndexWriter indexWriter;
    private static IndexReader indexReader;
    private static IndexSearcher indexSearcher;
    @Before
    public void init() throws IOException{
        Directory directory = FSDirectory.open(new File("E:\\team\\indexLibrary").toPath());
        //默认使用StandardAnalyzer解析器  标准解析器不能解析中文词汇只能识别中文的单个字
        //使用IKAnalyzer自定义解析器
        IndexWriterConfig writerConfig = new IndexWriterConfig(new IKAnalyzer());
        indexWriter = new IndexWriter(directory,writerConfig);
        indexReader = DirectoryReader.open(directory);
        indexSearcher = new IndexSearcher(indexReader);
    }
    /*
    给为索引库中添加文件
     */
    @Test
    public void addDocument() throws IOException {
        //1.创建Director对象 指明索引库的位置
        //2.创建一个indexWriter对象
        //3.创建一个Document对象
        //4.给document对象添加域
        //5.将document对象添加到索引库中
        //6.关闭indexWrite对象
        Document document = new Document();
        Field fieldName = new TextField("name","添加文件",Field.Store.YES);
        Field fieldContent = new TextField("content","添加文件的内容", Field.Store.NO);
        Field fieldSize = new LongPoint("size",100l);
        Field fieldPath = new StoredField("size",100l);
        document.add(fieldName);
        document.add(fieldContent);
        indexWriter.addDocument(document);
    }
    /*
    删除索引库中所有文件
     */
    @Test
    public void delAll() throws IOException{
        indexWriter.deleteAll();
    }

    /*
    通过查询特定
     */
    @Test
    public void delDocumentByQuery() throws IOException{
        Document document = new Document();
        document.add(new TextField("name","更新文档1",Field.Store.YES));
        document.add(new TextField("name1","更新文档2",Field.Store.YES));
        document.add(new TextField("name2","更新文档3",Field.Store.YES));
        //查询内容有特定词汇的文件并且删除
        indexWriter.deleteDocuments(new Term("content","spring"));
        //查询内容有特定词汇的文件删除并且加上一个新的文件
        indexWriter.updateDocument(new Term("", "这是一个晴朗的早晨"),document);
    }
    /*
    根据范围查询文档
     */
    @Test
    public void RangSearch() throws Exception{
        Query query = LongPoint.newRangeQuery("size",0l,100l);
        printQuery(query);
    }
    /*
    对搜索的内容先分词，基于分词进行查询   类似与百度
     */
    @Test
    public  void QueryParse() throws Exception {
        QueryParser queryParser = new QueryParser("name",new IKAnalyzer());
        Query parse = queryParser.parse("全新的开发工具包");
        printQuery(parse);
    }
    public void printQuery(Query query) throws Exception{
        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc doc: scoreDocs
        ) {
            int doc1 = doc.doc;
            Document document = indexSearcher.doc(doc1);
            System.out.println(document.get("name"));
            System.out.println(document.get("context"));
            System.out.println(document.get("size"));
            System.out.println("---------------------------------------");
        }
    }
    /*
    关闭indexWriter、indexReader、indexSearch
     */
    @After
    public void closeIndexWriter() throws IOException{
        indexWriter.close();
    }
}
