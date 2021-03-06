package org.jboss.set.mjolnir.archive.mail.report;

import j2html.tags.DomContent;
import org.jboss.set.mjolnir.archive.domain.RegisteredUser;
import org.jboss.set.mjolnir.archive.ldap.LdapScanningBean;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.table;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.tr;

public class WhitelistedUsersReportTable implements ReportTable {

    private static final String NAME_LABEL = "Name";
    private static final String RESPONSIBLE_PERSON_LABEL = "Responsible person";
    private static final String REPORT_TABLE_TITLE = "Whitelisted Users";

    @Inject
    LdapScanningBean ldapScanningBean;

    @Override
    public String composeTable() {
        String html = div().with(
                h2(REPORT_TABLE_TITLE).withStyle(Styles.H2_STYLE),
                table().withStyle(Styles.TABLE_STYLE + Styles.TD_STYLE).with(
                        tr().with(
                                th(NAME_LABEL).withStyle(Styles.TH_STYLE),
                                th(RESPONSIBLE_PERSON_LABEL).withStyle(Styles.TH_STYLE)
                        ),
                        addWhitelistedUserRows(getWhitelistedUsers())
                ))
                .render();
        return html;
    }

    private static DomContent addWhitelistedUserRows(List<RegisteredUser> whitelistedUsers) {
        whitelistedUsers.sort(new GitHubNameComparator());
        return each(whitelistedUsers, user -> tr(
                td(ReportUtils.stringOrEmpty(user.getGithubName())).withStyle(Styles.TD_STYLE),
                td(ReportUtils.stringOrEmpty(user.getResponsiblePerson())).withStyle(Styles.TD_STYLE)
        ));
    }

    private List<RegisteredUser> getWhitelistedUsers() {
        return ldapScanningBean.getWhitelistedUsers();
    }

    static class GitHubNameComparator implements Comparator<RegisteredUser> {
        @Override
        public int compare(RegisteredUser u1, RegisteredUser u2) {
            return u1.getGithubName().compareToIgnoreCase(u2.getGithubName());
        }
    }
}
