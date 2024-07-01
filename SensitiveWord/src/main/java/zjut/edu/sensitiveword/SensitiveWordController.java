package zjut.edu.sensitiveword;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SensitiveWordController {

    private final static String sensitiveWordUrl = "https://api.openai.com/v1/chat/completions";
    private final static String sensitiveWordUrl2 = "http://a7a6b6492e317112038c1ba139a2dca9.api-forwards.com/v1/chat/completions";

    private final static String apiKey = "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";
    final static String str1 = "最后一段user信息是一段文字，可能包含一些敏感信息，包括但不限于设计个人隐私（姓名、性别、年龄、电话、银行卡号、地址、各类密码、身高体重、性别种族性取向特征等等数据）、政治、暴力色情内容。你要做的是将其中所有敏感信息替换为字符“*”然后返回替换后的全文。注意，除了替换后的全文外你不能返回任何额外内容！！！不允许出现多余符号，一个字都不能多！注意一些身份的称呼只要不涉及具体的隐私就不要屏蔽，比如“老师”、”学生“之类的。并且也不是所有负面词语都要屏蔽，比如”反对“、”不认可“，不要那么严格，兴趣爱好不屏蔽（看书，听歌之类的）,广为人知的文艺作品不允许屏蔽！";
    final static String str2 = "这里给出一些参考示例，屏蔽严格程度就按这个来：涉及隐私：1：“张三老师您好”变为：“**老师您好”、2：“老师您好”变为“老师您好”、3：“我家住在成华大道”变成“我家住在****”、4：“我叫亚当兰伯特”变成“我叫“***””；涉及政治：1：“我反对独裁专政”变为“我反对****”；2：“人民当家作主”变为“人民当家做主”；涉及暴力：1：”把那个人打的满地找牙“变为“把那个人******”";
    final static String str3 = "注意，姓名、家庭住址、等信息必须屏蔽掉！必须用字符“*”屏蔽，不允许出现其它字符！不允许屏蔽兴趣爱好之类不敏感的隐私！不许屏蔽文艺作品！";

    @PostMapping("/SensitiveWord/")
    public Map<String, String> receiveBotMove(@RequestBody Map<String, String> data) throws IOException {
        HashMap<String, String> map = new HashMap<>();

        // 定义字符数组
        String[] prompts = {str1, str2, str3};

        // 构造请求体
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", "gpt-3.5-turbo-16k");
        JSONArray messagesArray = new JSONArray();
        jsonObject.put("messages", messagesArray);

        // 遍历字符数组添加提示词
        for (String prompt : prompts) {
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", prompt);
            jsonObject.getJSONArray("messages").add(systemMessage);
        }

        // 添加用户提供的文本
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", data.get("text"));
        jsonObject.getJSONArray("messages").add(userMessage);

        String requestBody = jsonObject.toJSONString();

        // 创建 HttpClient 实例
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建 HttpPost 请求
        HttpPost httpPost = new HttpPost(sensitiveWordUrl2);

        // 设置请求头和请求体
        httpPost.setHeader("Authorization", "Bearer " + apiKey);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8)); // 指定UTF-8编码

        // 执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // 处理响应
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8); // 指定UTF-8编码

        // 解析响应
        String filteredContent = null;
        JSONObject responseJson = JSONObject.parseObject(responseBody);
        if (responseJson != null) {
            filteredContent = responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        }

        // 关闭资源
        response.close();
        httpClient.close();

        map.put("msg", filteredContent);

        return map;
    }
}
