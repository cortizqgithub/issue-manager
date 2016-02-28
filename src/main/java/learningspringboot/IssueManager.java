package learningspringboot;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.github.api.GitHubIssue;
import org.springframework.social.github.api.impl.GitHubTemplate;
import org.springframework.stereotype.Service;

@Service
public class IssueManager implements InitializingBean {
    @Value("${github.token}")
	String githubToken;
	
	@Value("${org}")
	String org;
	
	@Value("${repos}")
	String[] repos;
	
	GitHubTemplate gitHubTemplate;
	
	@Override
    public void afterPropertiesSet() throws Exception {
        this.gitHubTemplate = new GitHubTemplate(githubToken);
    }
	
	public List<Issue> findOpenIssues() {
		List<Issue> openIssues = new ArrayList<>();
		System.out.println("INSIDE IssueManager.findOpenIssues() method");
		for (String repo : repos) {
		    System.out.println("=========> Processing " + repo);
			final List<GitHubIssue> issues = gitHubTemplate.repoOperations().getIssues(org, repo);
			System.out.println("issues found");
			System.out.println("Storing issues");
			for (GitHubIssue issue : issues) {
				if (issue.getState().equals("open")) {
					openIssues.add(new Issue(repo, issue));
				}
			}
			System.out.println("issues found END <======");
		}
		System.out.println("method exit IssueManager.findOpenIssues() method");
		return openIssues;
	}
}