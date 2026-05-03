export default function InfoBox({ children }) {
  return (
    <div className="flex items-start gap-1.5 bg-teal-50 border border-teal-100 rounded-lg px-3 py-2 text-xs text-teal-700 mt-2">
      <span>ℹ️</span>
      <span>{children}</span>
    </div>
  );
}