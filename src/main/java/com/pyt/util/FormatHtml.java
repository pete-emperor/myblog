package com.pyt.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by PC on 2020/7/2.
 */
public class FormatHtml {

    public static void main(String arg[]){
        String html = "<div id=\"cnblogs_post_body\" class=\"blogpost-body cnblogs-markdown\">    <p>当用户运用于Spark自己依附统一个库时可能会爆发依附抵触，招致法度模范奔溃。依附抵触显现为在运行中泛起NoSuchMethodError或者ClassNotFoundException的异常或者其他与类加载相关的JVM异常。</p><p>此时，若能确定classpath中存在这个包，则缺陷是因为classpath中存在2个分歧版本的jar包了，好比罕有的log4j，你在classpath中添加了log4j.jar，而spark的lib名目中也有log4j.jar，而且这2个jar包版本纷歧致的话，就会泛起依附抵触问题。</p><p>解决编制有2种：</p><ol><li>修正你的运用，使其运用的依附库的版本与Spark所运用的相同。</li><li>运用称为shading的编制打包你的运用。运用maven-shade-plugin插件进行初级设置来支持这种打包编制。shading可以让你以另一种命名空间保留抵触的包，并自动重写运用的代码使得它们运用重命名后的版本。这种技巧有些简略粗犷，不外对解决运行时依附抵触的问题很是有用。</li></ol><blockquote><p>Java 工程经常会碰到第三方Jar 包抵触，运用 maven-shade-plugin 解决 jar 或类的多版本抵触。 maven-shade-plugin 在打包时，可以将项目中依附的 jar 包中的一些类文件打包到项目构建生成的 jar 包中，在打包的时刻把类重命名。</p></blockquote><p>下面的设置将org.codehaus.plexus.util jar 包重命名为org.shaded.plexus.util。</p><pre><code>&lt;build&gt;     &lt;plugins&gt;      &lt;plugin&gt;        &lt;groupId&gt;org.apache.maven.plugins&lt;/groupId&gt;        &lt;artifactId&gt;maven-shade-plugin&lt;/artifactId&gt;        &lt;version&gt;2.4.3&lt;/version&gt;        &lt;executions&gt;          &lt;execution&gt;            &lt;phase&gt;package&lt;/phase&gt;            &lt;goals&gt;              &lt;goal&gt;shade&lt;/goal&gt;            &lt;/goals&gt;            &lt;configuration&gt;              &lt;relocations&gt;                &lt;relocation&gt;                    &lt;pattern&gt;org.codehaus.plexus.util&lt;/pattern&gt;                    &lt;shadedPattern&gt;org.shaded.plexus.util&lt;/shadedPattern&gt;               &lt;excludes&gt;                &lt;exclude&gt;org.codehaus.plexus.util.xml.Xpp3Dom&lt;/exclude&gt;                &lt;exclude&gt;org.codehaus.plexus.util.xml.pull.*&lt;/exclude&gt;               &lt;/excludes&gt;                &lt;/relocation&gt;              &lt;/relocations&gt;            &lt;/configuration&gt;          &lt;/execution&gt;        &lt;/executions&gt;      &lt;/plugin&gt;    &lt;/plugins&gt;  &lt;/build&gt;</code></pre><p>主如果<relocations>标签如下，个中可以有并列的多个<relocation>标签，也可以没有<excludes>标签进行消除。</p></div>";
       // html = html.replace("&lt;","<").replace("&gt;",">");
        Document doc = Jsoup.parseBodyFragment(html);
        html = doc.body().html();
        System.out.println(html);
    }


}
