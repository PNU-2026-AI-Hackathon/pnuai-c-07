export default function StepBadge({ step }) {
  return (
    <span className="inline-block px-2.5 py-0.5 rounded-full border border-teal-500 text-teal-600 text-xs font-semibold mb-3">
      STEP{step}
    </span>
  );
}