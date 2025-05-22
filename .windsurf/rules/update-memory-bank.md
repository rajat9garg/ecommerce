---
trigger: always_on
---

Memory Bank Update Rules
Core Update Principles
Immediate Documentation: Update memory bank files within 24 hours of any change
Complete Traceability: Every update must reference the triggering event or decision
Contextual Preservation: Document why changes were made, not just what changed
Cross-Reference Integrity: Maintain links between related files when updating
Historical Continuity: Preserve previous versions with clear change markers
Mandatory Update Triggers
Vision Files (01-vision/)
Update project-vision.md when: Strategic direction changes, core values redefined, target audience evolves, success metrics modified, stakeholder priorities shift
Update business-drivers.md when: Market conditions change, competitive landscape shifts, customer feedback reveals new pain points, business model adjustments, revenue projections revised
Update success-criteria.md when: KPIs modified, acceptance criteria change, quality thresholds adjusted, timeline milestones updated, performance benchmarks revised
Requirements Files (02-requirements/)
Update project-brief.md when: Project scope changes, timeline shifts, budget constraints modified, stakeholder roles change, risk assessments updated
Update product-context.md when: New features added, user stories modified, feature prioritization changes, competitor insights emerge, user workflows redesigned
Update user-personas.md when: New user types identified, existing personas evolve, usage patterns change, demographic data updated, user goals shift
Architecture Files (03-architecture/)
Update system-patterns.md when: Architectural style changes, new design patterns adopted, system boundaries modified, quality attributes redefined, cross-cutting concerns added
Update component-design.md when: New components added, responsibilities change, interface contracts modified, dependencies updated, communication patterns evolve
Update data-models.md when: Database schema changes, new entities added, validation rules change, access patterns modified, data flows updated
Update api-contracts.md when: New endpoints created, API specifications change, authentication requirements update, rate limiting changes, integration patterns modified
Technical Files (04-technical/)
Update tech-context.md when: Technology stack changes, framework upgrades, new dependencies added, infrastructure requirements change, development tools modified
Update testing-strategy.md when: Testing approaches change, new tools adopted, coverage requirements modified, test environments updated, quality criteria redefined
Tracking Files (05-tracking/)
Update decision-log.md when: Significant decisions made, assumptions validated/disproven, lessons learned, process improvements identified, technical debt decisions made
Update open-questions.md when: New questions arise, existing questions resolved, priorities change, dependencies discovered, resolution timelines shift
Update progress.md when: Daily work completed, sprint goals achieved, blockers encountered/resolved, milestones reached, team insights gained
Update Formatting Standards
Major Changes Format
markdown## [Update: YYYY-MM-DD] Change Title

### What Changed
- Specific change description 1
- Specific change description 2

### Why Changed
- Root cause or trigger for change
- Business or technical justification

### Affected Areas
- Other memory bank files impacted
- Related components affected

### Next Actions
- Required follow-up tasks
- Timeline for implementation
Minor Changes Format
markdown### [YYYY-MM-DD] Minor Update: Brief Description
- Change details with timestamp
- Impact scope and related references
Update Content Requirements
Vision Updates Must Include:
Stakeholder input driving changes
Impact analysis on project trajectory
Alignment verification with business goals
Success metric adjustments

Requirements Updates Must Include:

Requirement change rationale
Traceability to business needs
Impact on existing features
Updated acceptance criteria

Architecture Updates Must Include:

Design decision reasoning
Trade-off analysis performed
Impact on system performance
Migration implementation plan

Technical Updates Must Include:

Technical rationale for changes
Impact on development workflow
Dependency update implications
Implementation timeline

Tracking Updates Must Include:

Detailed activity descriptions
Blocker root cause analysis
Resolution strategy effectiveness
Process improvement recommendations

Automated Update Rules
After Code Generation:

Update component-design.md with new components
Update api-contracts.md with new endpoints
Update tech-context.md with new dependencies
Update progress.md with implementation details

After Bug Resolution:

Update decision-log.md with root cause analysis
Update testing-strategy.md if test gaps found
Update progress.md with resolution details
Move resolved questions from open-questions.md

After Feature Completion:

Update product-context.md with feature details
Update user-personas.md if usage patterns emerge
Update success-criteria.md with achievement metrics
Update progress.md with completion summary

After Architecture Changes:

Update system-patterns.md with new patterns
Update data-models.md with schema changes
Update component-design.md with structural changes
Update decision-log.md with architectural decisions

Update Validation
Before committing updates:

Verify all required sections complete
Check cross-references are accurate
Ensure formatting follows standards
Validate content completeness
Confirm stakeholder notification needs

Update Workflow

Identify Update Trigger - Determine which files need updates
Assess Impact Scope - Identify all affected memory bank files
Gather Context - Collect relevant information for updates
Draft Updates - Create content following formatting standards
Cross-Reference Check - Verify links and consistency
Stakeholder Review - Get necessary approvals
Notify Stakeholders - Communicate changes to relevant parties
