import StepBadge from "./StepBadge";

// step 제목/설명 공통 레이아웃
export default function StepLayout({ step, title, sub, children }) {
  return (
    <div>
      <StepBadge step={step} />
      <h2 className="text-lg font-bold text-gray-800 mb-1">{title}</h2>
      <p className="text-sm text-gray-400 mb-4">{sub}</p>
      {children}
    </div>
  );
}