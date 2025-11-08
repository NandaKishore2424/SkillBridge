import React from 'react';
import { Link } from 'react-router-dom';
import { FaArrowRight, FaAward, FaBriefcase, FaChartLine, FaGraduationCap, FaLaptopCode, FaLock, FaUsers } from 'react-icons/fa';

const Home = () => {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navigation */}
      <nav className="bg-white/80 backdrop-blur border-b border-gray-100 sticky top-0 z-40">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <Link to="/" className="flex-shrink-0 flex items-center">
                <span className="text-2xl font-bold text-primary-600 tracking-tight">SkillBridge</span>
              </Link>
            </div>
            <div className="flex items-center space-x-4">
              <Link to="/login" className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-primary-600 transition">
                Login
              </Link>
              <Link to="/register" className="px-4 py-2 rounded-md text-sm font-medium bg-primary-600 text-white hover:bg-primary-700 transition">
                Register
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero section */}
      <section className="relative overflow-hidden bg-gradient-to-br from-primary-900 via-primary-800 to-primary-600">
        <div className="absolute inset-0 opacity-20 bg-[radial-gradient(circle_at_top,_#ffffff_0%,_transparent_60%)]" />
        <div className="absolute -top-24 -right-24 w-96 h-96 rounded-full bg-primary-500/20 blur-3xl" />
        <div className="absolute -bottom-32 -left-32 w-[32rem] h-[32rem] rounded-full bg-primary-400/10 blur-3xl" />

        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24 lg:py-32 flex flex-col lg:flex-row items-center gap-16">
          <div className="max-w-2xl">
            <div className="inline-flex items-center px-3 py-1 rounded-full bg-white/10 text-sm text-white/80 backdrop-blur border border-white/20">
              End-to-end talent acceleration platform
            </div>
            <h1 className="mt-6 text-4xl sm:text-5xl lg:text-6xl font-bold tracking-tight text-white leading-tight">
              Build job-ready tech talent with precision and speed.
            </h1>
            <p className="mt-6 text-lg sm:text-xl text-white/80 leading-relaxed">
              SkillBridge unifies cohorts, mentors, and hiring pipelines so universities and companies can grow future-ready talent with measurable outcomes.
            </p>
            <div className="mt-10 flex flex-wrap gap-4">
              <Link
                to="/register"
                className="px-6 py-3 rounded-md text-base font-semibold text-primary-900 bg-white shadow-lg hover:shadow-xl transition transform hover:-translate-y-0.5"
              >
                Launch Your Cohort
              </Link>
              <Link
                to="/login"
                className="px-6 py-3 rounded-md text-base font-semibold text-white border border-white/40 hover:bg-white/10 transition"
              >
                Explore the Platform
              </Link>
            </div>
            <div className="mt-12 grid grid-cols-2 gap-6 text-white/70 text-sm">
              <div>
                <p className="text-3xl font-semibold text-white">+12k</p>
                <p>Students placed through SkillBridge cohorts</p>
              </div>
              <div>
                <p className="text-3xl font-semibold text-white">94%</p>
                <p>Average placement rate across graduating batches</p>
              </div>
            </div>
          </div>
          <div className="relative w-full max-w-xl">
            <div className="absolute inset-0 rounded-3xl bg-gradient-to-tr from-white/20 via-white/5 to-transparent blur-xl" />
            <div className="relative rounded-3xl border border-white/10 bg-white/10 backdrop-blur-xl shadow-2xl p-8">
              <div className="flex items-center justify-between mb-6">
                <span className="text-white/80 text-sm font-medium">Active cohorts</span>
                <span className="text-white text-lg font-semibold">Q4 · 2025</span>
              </div>
              <div className="space-y-6">
                {[
                  { title: 'Full-Stack Accelerator', mentor: 'Ananya Rao', progress: 78, learners: 38 },
                  { title: 'AI & Data Engineering', mentor: 'Vikram Patel', progress: 64, learners: 26 },
                  { title: 'Cloud-native DevOps', mentor: 'Riya Sethi', progress: 52, learners: 19 }
                ].map((cohort) => (
                  <div key={cohort.title} className="bg-white/5 rounded-2xl p-5 border border-white/10">
                    <div className="flex justify-between items-center">
                      <div>
                        <p className="text-white font-semibold">{cohort.title}</p>
                        <p className="text-white/60 text-sm">Mentor: {cohort.mentor}</p>
                      </div>
                      <span className="text-white/80 text-sm">{cohort.learners} learners</span>
                    </div>
                    <div className="mt-4">
                      <div className="flex justify-between text-xs text-white/60 mb-1">
                        <span>Progress</span>
                        <span>{cohort.progress}%</span>
                      </div>
                      <div className="h-2.5 bg-white/10 rounded-full">
                        <div
                          className="h-2.5 bg-gradient-to-r from-emerald-400 to-primary-300 rounded-full"
                          style={{ width: `${cohort.progress}%` }}
                        />
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features section */}
      <section className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto">
            <h2 className="text-3xl font-bold text-gray-900 sm:text-4xl">
              One operating system for talent development
            </h2>
            <p className="mt-4 text-lg text-gray-600">
              SkillBridge orchestrates every touchpoint—from skills assessment to offer letter—so you can deliver cohorts that hiring managers trust.
            </p>
          </div>

          <div className="mt-16 grid grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-3">
            {[
              {
                icon: <FaGraduationCap className="h-6 w-6" />,
                title: 'Skill diagnostics at scale',
                copy: 'Run adaptive assessments that benchmark every learner against industry frameworks before the cohort begins.'
              },
              {
                icon: <FaLaptopCode className="h-6 w-6" />,
                title: 'Immersive cohort delivery',
                copy: 'Deliver structured learning paths curated by mentors, with hands-on labs, code reviews, and weekly retros.'
              },
              {
                icon: <FaUsers className="h-6 w-6" />,
                title: 'Mentor intelligence',
                copy: 'Match students with mentor pods that optimise for experience, availability, and learner persona fit.'
              },
              {
                icon: <FaChartLine className="h-6 w-6" />,
                title: 'Outcome analytics',
                copy: 'Track capability uplift with cohort health dashboards, employer-ready scorecards, and placement signals.'
              },
              {
                icon: <FaBriefcase className="h-6 w-6" />,
                title: 'Employer network',
                copy: 'Connect cohorts with hiring partners through curated demo days, project showcases, and challenge sprints.'
              },
              {
                icon: <FaLock className="h-6 w-6" />,
                title: 'Enterprise-grade governance',
                copy: 'SSO, audit trails, granular roles, and compliance-ready infrastructure hosted on Supabase/PostgreSQL.'
              }
            ].map((feature) => (
              <div key={feature.title} className="group relative overflow-hidden rounded-3xl border border-gray-100 bg-white p-8 shadow-sm transition hover:-translate-y-1 hover:shadow-xl">
                <div className="absolute inset-x-0 top-0 h-1 bg-gradient-to-r from-primary-400 via-emerald-400 to-primary-500 opacity-0 transition group-hover:opacity-100" />
                <div className="flex h-12 w-12 items-center justify-center rounded-full bg-primary-50 text-primary-600">
                  {feature.icon}
                </div>
                <h3 className="mt-6 text-xl font-semibold text-gray-900">{feature.title}</h3>
                <p className="mt-3 text-sm text-gray-600 leading-relaxed">{feature.copy}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Credibility */}
      <section className="py-16 bg-gray-50">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="bg-white rounded-3xl border border-gray-100 p-8 shadow-sm">
              <FaAward className="h-8 w-8 text-primary-500" />
              <h3 className="mt-5 text-lg font-semibold text-gray-900">360° learning assurance</h3>
              <p className="mt-3 text-sm text-gray-600">
                Every cohort led by mentors with 8+ years of industry experience and validated teaching playbooks.
              </p>
            </div>
            <div className="bg-white rounded-3xl border border-gray-100 p-8 shadow-sm">
              <FaUsers className="h-8 w-8 text-primary-500" />
              <h3 className="mt-5 text-lg font-semibold text-gray-900">Partner ecosystem</h3>
              <p className="mt-3 text-sm text-gray-600">
                Trusted by universities, skilling bootcamps, and enterprise academies for accelerated hiring pipelines.
              </p>
            </div>
            <div className="bg-white rounded-3xl border border-gray-100 p-8 shadow-sm">
              <FaChartLine className="h-8 w-8 text-primary-500" />
              <h3 className="mt-5 text-lg font-semibold text-gray-900">Verified outcomes</h3>
              <p className="mt-3 text-sm text-gray-600">
                Standardised success metrics with employer feedback loops ensure consistent, audit-ready reporting.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Testimonial section */}
      <section className="py-20 bg-white">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid gap-12 lg:grid-cols-2 items-center">
            <div>
              <p className="text-sm uppercase tracking-widest text-primary-500 font-semibold">What our partners say</p>
              <h2 className="mt-4 text-3xl sm:text-4xl font-bold text-gray-900">
                “SkillBridge is the backbone of our graduate talent pipeline.”
              </h2>
              <p className="mt-4 text-lg text-gray-600 leading-relaxed">
                “We transitioned from fragmented spreadsheets to a single platform that unites assessments, training, and employer engagement. Our placement velocity improved by 36% in one quarter.”
              </p>
              <div className="mt-8">
                <p className="text-lg font-semibold text-gray-900">Devika Sharma</p>
                <p className="text-sm text-gray-500">Head of Talent Transformation · NovaTech Solutions</p>
              </div>
            </div>
            <div className="bg-gray-50 border border-gray-100 rounded-3xl p-10 shadow-inner">
              <div className="grid gap-8 sm:grid-cols-3 text-center">
                {[
                  { metric: '18+', label: 'Partner Institutions' },
                  { metric: '220+', label: 'Industry Mentors' },
                  { metric: '48 hrs', label: 'Average time-to-offer' }
                ].map((item) => (
                  <div key={item.label}>
                    <p className="text-3xl font-bold text-primary-600">{item.metric}</p>
                    <p className="mt-2 text-sm text-gray-600">{item.label}</p>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* How it works */}
      <section className="py-20 bg-gray-50">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-2xl mx-auto">
            <h2 className="text-3xl sm:text-4xl font-bold text-gray-900">From classroom to career, simplified</h2>
            <p className="mt-4 text-lg text-gray-600">Launch your first cohort in less than a week with guided onboarding and pre-built templates.</p>
          </div>
          <div className="mt-16 grid gap-8 md:grid-cols-2 lg:grid-cols-4">
            {[
              { step: '01', title: 'Assess & Map', copy: 'Import learners, run diagnostics, and auto-assign cohorts based on skill gaps.' },
              { step: '02', title: 'Coach & Build', copy: 'Deliver sprints, live sessions, and project reviews in one collaborative workspace.' },
              { step: '03', title: 'Benchmark & Iterate', copy: 'Track progress with pulse surveys, mentor notes, and placement readiness scores.' },
              { step: '04', title: 'Showcase & Hire', copy: 'Host virtual demo days, share talent profiles, and convert interviews into offers.' }
            ].map((stage) => (
              <div key={stage.step} className="relative rounded-3xl border border-gray-100 bg-white p-8 shadow-sm">
                <span className="absolute -top-4 left-6 inline-flex h-8 w-8 items-center justify-center rounded-full bg-primary-600 text-white text-sm font-semibold">
                  {stage.step}
                </span>
                <h3 className="mt-6 text-xl font-semibold text-gray-900">{stage.title}</h3>
                <p className="mt-3 text-sm text-gray-600 leading-relaxed">{stage.copy}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA section */}
      <section className="relative overflow-hidden bg-gradient-to-r from-primary-700 via-primary-600 to-primary-800">
        <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_center,_rgba(255,255,255,0.2),_rgba(255,255,255,0))]" />
        <div className="relative max-w-6xl mx-auto py-16 px-4 sm:px-6 lg:px-8">
          <div className="grid gap-10 lg:grid-cols-2 items-center">
            <div>
              <h2 className="text-3xl sm:text-4xl font-bold text-white">Ready to build your next high-performing cohort?</h2>
              <p className="mt-4 text-lg text-primary-100 leading-relaxed">
                Our team will help you configure SkillBridge for your context—be it a university finishing school, bootcamp, or enterprise academy.
              </p>
            </div>
            <div className="flex flex-col sm:flex-row gap-4 justify-end">
              <Link
                to="/register"
                className="inline-flex items-center justify-center px-6 py-3 rounded-md text-base font-semibold text-primary-700 bg-white shadow-lg hover:bg-gray-100 transition"
              >
                Schedule onboarding <FaArrowRight className="ml-2 h-4 w-4" />
              </Link>
              <Link
                to="/login"
                className="inline-flex items-center justify-center px-6 py-3 rounded-md text-base font-semibold text-white border border-white/40 hover:bg-white/10 transition"
              >
                View product tour
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gray-900 text-gray-400">
        <div className="max-w-6xl mx-auto py-12 px-4 sm:px-6 lg:px-8">
          <div className="grid gap-10 md:grid-cols-4">
            <div>
              <h3 className="text-white text-xl font-semibold">SkillBridge</h3>
              <p className="mt-4 text-sm leading-relaxed">
                The mission control for institutions and companies accelerating tech talent—powered by Supabase, Spring Boot, and React.
              </p>
            </div>
            <div>
              <h4 className="text-white text-sm font-semibold tracking-wider uppercase">Platform</h4>
              <ul className="mt-4 space-y-3 text-sm">
                <li>Learning Experience</li>
                <li>Mentor Workspace</li>
                <li>Placement Hub</li>
                <li>Analytics Studio</li>
              </ul>
            </div>
            <div>
              <h4 className="text-white text-sm font-semibold tracking-wider uppercase">Use cases</h4>
              <ul className="mt-4 space-y-3 text-sm">
                <li>University finishing school</li>
                <li>Bootcamp cohort management</li>
                <li>Corporate academy</li>
                <li>Government skilling programs</li>
              </ul>
            </div>
            <div>
              <h4 className="text-white text-sm font-semibold tracking-wider uppercase">Compliance</h4>
              <ul className="mt-4 space-y-3 text-sm">
                <li>Secure Supabase PostgreSQL storage</li>
                <li>Role-based access control</li>
                <li>GDPR & FERPA aligned data policies</li>
                <li>24x7 uptime monitoring</li>
              </ul>
            </div>
          </div>
          <div className="mt-12 border-t border-white/10 pt-8 text-sm text-gray-500">
            &copy; {new Date().getFullYear()} SkillBridge. All rights reserved.
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Home;