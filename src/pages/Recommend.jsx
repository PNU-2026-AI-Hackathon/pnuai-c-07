import Header from "../components/Header";
import useRecommendForm from "../hooks/UseRecommendFrom";
import {
  StepRegion,
  StepBasicInfo,
  StepBenefits,
  StepPersonalInfo,
  StepHouseholdIncome,
  StepHousing,
  StepEmployment,
  StepSavingPlan,
} from "../components/RecommendSteps";
import { useState } from "react";

export default function Recommend() {
  const { step, formData, setFormData, cats, loading, go, handleSubmit } = useRecommendForm();
  const [isOpen, setIsOpen] = useState(false);

  if (loading) {
    return (
      <div> 로딩 중...임시 </div>
    );
  }

  const steps = [
    <StepRegion          data={formData} setData={setFormData} cats={cats} onNext={go(1)} />,
    <StepBasicInfo       data={formData} setData={setFormData} cats={cats} onPrev={go(0)} onNext={go(2)} />,
    <StepBenefits        data={formData} setData={setFormData} cats={cats} onPrev={go(1)} onNext={go(3)} />,
    <StepPersonalInfo    data={formData} setData={setFormData}             onPrev={go(2)} onNext={go(4)} />,
    <StepHouseholdIncome data={formData} setData={setFormData} cats={cats} onPrev={go(3)} onNext={go(5)} />,
    <StepHousing         data={formData} setData={setFormData}             onPrev={go(4)} onNext={go(6)} />,
    <StepEmployment      data={formData} setData={setFormData}             onPrev={go(5)} onNext={go(7)} />,
    <StepSavingPlan      data={formData} setData={setFormData} cats={cats} onPrev={go(6)} onSubmit={handleSubmit} />,
  ];

  return (
    <div className="min-h-screen bg-teal-50/40 flex flex-col">
      <Header />

      <div className="flex-1 flex flex-col items-center px-4 pt-16">

        {/* 타이틀 */}
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-800">
            내게 딱 맞는 <span className="text-teal-500">금융상품,</span>
          </h1>
          <h2 className="text-3xl font-bold text-teal-500">y-Fin이 찾아줘요</h2>
          <p className="text-sm text-gray-400 mt-4">
            수백 개 은행 상품을 일일이 비교할 필요 없이, 키워드만 선택하면<br />
            최적의 상품을 찾을 수 있어요.
          </p>
        </div>

        {/* 검색바 + 폼 */}
        <div className="w-full max-w-2xl bg-white rounded-2xl shadow-sm border border-gray-100">

          {/* 검색바 */}
          <button
            type="button"
            onClick={() => setIsOpen((prev) => !prev)}
            className="w-full flex items-center justify-between px-5 py-4 hover:bg-gray-50 transition-colors rounded-2xl"
          >
            <div className="flex items-center gap-3 text-gray-400">
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                  d="M21 21l-4.35-4.35M17 11A6 6 0 1 1 5 11a6 6 0 0 1 12 0z" />
              </svg>
              <span className="text-sm">키워드로 금융상품 찾기</span>
            </div>
            <div className="w-8 h-8 rounded-full bg-teal-500 flex items-center justify-center flex-shrink-0">
              <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
              </svg>
            </div>
          </button>

          {/* 폼 */}
          {isOpen && (
            <div className="px-8 pb-8 border-t border-gray-100 pt-6">
              {steps[step]}
            </div>
          )}

        </div>
      </div>
    </div>
  );
}