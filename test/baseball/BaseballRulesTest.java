package baseball;

final class BaseballRulesTest {
    void run() {
        validatesCurrentRuleConfiguration();
    }

    private void validatesCurrentRuleConfiguration() {
        BaseballRules.validate();
    }
}
